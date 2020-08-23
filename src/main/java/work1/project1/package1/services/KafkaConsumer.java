package work1.project1.package1.services;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import work1.project1.package1.configuration.Mapper;
import work1.project1.package1.dto.request.EmployeeAddRequest;
import work1.project1.package1.dto.request.EmployeeKafka;
import work1.project1.package1.dto.response.EmployeeCompleteResponse;
import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.exception.NotPresentException;
import work1.project1.package1.repository.EmployeeRepository;

import javax.validation.Valid;

@Service
@Validated
public class KafkaConsumer {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    Caching caching;
    @Autowired
    ModelMapper mapper;

    @KafkaListener(topics = "kafkaTopic2",  groupId="group_json",containerFactory = "kafkaListenerContainerFactory")
    public void consumeJson(@Valid EmployeeKafka employeeKafka) throws CustomException, NotPresentException {
       // System.out.println(object +"   .....   ");
       // EmployeeKafka employeeKafka=mapper.map(object,EmployeeKafka.class);
        Long employeeId = employeeKafka.getEmployeeId();
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId).orElse(null);
        if (employeeEntity != null) {
            if(employeeKafka.getSalary()>=0)
            employeeEntity.setSalary(employeeKafka.getSalary());
            caching.updateEmployeeCache(employeeId,employeeEntity);
            employeeRepository.save(employeeEntity);
            return;
        }
        if(employeeKafka.getName()==null || employeeKafka.getPhone()==null || employeeKafka.getSalary()<=0 || employeeKafka.getCompanyId()==-1
        || employeeKafka.getDepartmentId()==-1) {
            System.out.println(" invalid inputs fetched from kafka topic!! ");
            return;
        }
        EmployeeAddRequest employeeAddRequest= mapper.map(employeeKafka,EmployeeAddRequest.class);
        employeeService.addEmployee(employeeAddRequest,0L);
        return;
    }
    

}

