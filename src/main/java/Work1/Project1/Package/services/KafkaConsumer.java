package Work1.Project1.Package.services;
import Work1.Project1.Package.entity.EmployeeEntity;
import Work1.Project1.Package.exception.CustomException;
import Work1.Project1.Package.request.RequestEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static Work1.Project1.Package.constants.ApplicationConstants.Add_Failed;

@Service
public class KafkaConsumer {


    @Autowired
    EmployeeServices employeeServices;

    @KafkaListener(topics = "testTopic",  groupId="group_id",containerFactory = "employeeEntityKafkaListenerContainerFactory")
    public void consumeJson(EmployeeEntity employeeEntity) throws CustomException {

        RequestEmployee requestEmployee=new RequestEmployee(employeeEntity.getEmployeePK().getCompanyId(),
                employeeEntity.getEmployeePK().getDepartmentId(), employeeEntity.getEmpName(),employeeEntity.getPhone(),
                employeeEntity.getSalary());
        if(employeeServices.addEmployee(requestEmployee).getStatus()==404) //if add failed means status code 404 in response
        {
            employeeServices.updateDetails(employeeEntity.getEmployeePK().getCompanyId(),employeeEntity.getEmployeePK().getDepartmentId(),
                    employeeEntity.getEmployeePK().getCompanyId(), employeeEntity.getEmpName(),employeeEntity.getPhone(),employeeEntity.getSalary());
        }
    }

}

