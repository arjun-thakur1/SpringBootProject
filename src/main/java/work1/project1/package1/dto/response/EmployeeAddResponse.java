package work1.project1.package1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import work1.project1.package1.entity.EmployeeEntity;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class EmployeeAddResponse {  //get response

    @Getter @Setter private Long id;
    @Getter @Setter private String name;
    @Getter @Setter private String phone;

    @Getter @Setter private  String accessToken;

    @Getter @Setter private String message=SUCCESS;

    public EmployeeAddResponse convert(EmployeeEntity employeeEntity) {
        EmployeeAddResponse employeeAddResponse=new EmployeeAddResponse();

        employeeAddResponse.setId(employeeEntity.getId());
        employeeAddResponse.setName(employeeEntity.getName());
        employeeAddResponse.setPhone(employeeEntity.getPhone());
       // employeeAddResponse.setAccessToken(token);

       return employeeAddResponse;
    }

}
