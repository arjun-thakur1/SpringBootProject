package work1.project1.package1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class EmployeeAddResponseDto {  //get response

    @Getter @Setter private Long id;
    @Getter @Setter private String name;
    @Getter @Setter private String phone;
   // @Getter @Setter private Long salary;
    //@Getter @Setter private Long managerId;
    //@Getter @Setter private  String designation;

     @Getter @Setter private String message=SUCCESS;

}
