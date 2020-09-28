package work1.project1.package1.dto.response;

import lombok.*;
import org.springframework.stereotype.Service;
import work1.project1.package1.entity.EmployeeEntity;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Service
public class EmployeeAddResponse {  //get response

     private Long id;
     private String name;
     private String phone;

   // @Getter @Setter private  String accessToken;

     private String message=SUCCESS;

    public EmployeeAddResponse convert(EmployeeEntity employeeEntity) {
        EmployeeAddResponse employeeAddResponse=new EmployeeAddResponse();

        employeeAddResponse.setId(employeeEntity.getId());
        employeeAddResponse.setName(employeeEntity.getName());
        employeeAddResponse.setPhone(employeeEntity.getPhone());
       // employeeAddResponse.setAccessToken(token);

       return employeeAddResponse;
    }

}
