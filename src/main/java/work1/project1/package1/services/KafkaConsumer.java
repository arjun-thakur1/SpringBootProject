package work1.project1.package1.services;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import work1.project1.package1.dto.request.EmployeeAddRequest;
import work1.project1.package1.dto.request.EmployeeKafka;
import work1.project1.package1.entity.EmployeeEntity;
import work1.project1.package1.exception.CustomException;
import work1.project1.package1.exception.InsufficientDataException;
import work1.project1.package1.exception.NotPresentException;
import work1.project1.package1.repository.EmployeeRepository;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@Service
@Validated
public class KafkaConsumer {
/*
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CachingService caching;
    @Autowired
    ModelMapper mapper;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(topics = "${kafka.topic}",  groupId="${kafka.groupId}",containerFactory = "kafkaListenerContainerFactory")
    public void consumeJson(@Valid EmployeeKafka employeeKafka) throws CustomException, NotPresentException,
            NoSuchAlgorithmException, InsufficientDataException {

        Long employeeId = employeeKafka.getEmployeeId();
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId).orElse(null);
        if (employeeEntity != null) {
            if(employeeKafka.getSalary()>=0)
            employeeEntity.setSalary(employeeKafka.getSalary());
            employeeEntity.setUpdatedBy(0L);
            caching.updateEmployeeCache(employeeId,employeeEntity);
            employeeRepository.save(employeeEntity);
            return;
        }
        if(employeeKafka.getName()==null || employeeKafka.getPhone()==null || employeeKafka.getSalary()==null ||
           employeeKafka.getSalary()<=0 || employeeKafka.getCompanyId()==null || employeeKafka.getDepartmentId()==null) {
           logger.info("invalid inputs fetched from kafka topic!!");
           return;
        }
        EmployeeAddRequest employeeAddRequest= mapper.map(employeeKafka,EmployeeAddRequest.class);
        employeeService.addEmployee(employeeAddRequest,0L);
        return;
    }

 */


}

