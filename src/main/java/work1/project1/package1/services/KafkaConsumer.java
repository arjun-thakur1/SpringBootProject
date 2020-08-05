package work1.project1.package1.services;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

/*
    @Autowired
    EmployeeServices employeeServices;

    @KafkaListener(topics = "projectTopic",  groupId="group_id",containerFactory = "employeeEntityKafkaListenerContainerFactory")
    public void consumeJson(KafkaRequestConsume kafkaRequestConsume) throws CustomException {

        RequestEmployee requestEmployee=new RequestEmployee(kafkaRequestConsume.getCompanyId(),
                kafkaRequestConsume.getDepartmentId(), kafkaRequestConsume.getEmpName(),kafkaRequestConsume.getPhone(),
                kafkaRequestConsume.getSalary());

        if(employeeServices.addEmployee(requestEmployee).getStatus()==404) //if add failed means status code 404 in response
        {
            employeeServices.updateDetails(kafkaRequestConsume.getCompanyId(),kafkaRequestConsume.getDepartmentId(),
                    kafkaRequestConsume.getEmployeeId(), kafkaRequestConsume.getEmpName(),kafkaRequestConsume.getPhone(),kafkaRequestConsume.getSalary());
        }
    }
*/
}

