package work1.project1.package1.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static work1.project1.package1.constants.ApplicationConstants.EMPLOYEE;
import static work1.project1.package1.constants.ApplicationConstants._NONE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeAddRequest {

    @NotNull
    private String name;

    @NotNull
    @Pattern(regexp = "^[0-9]{10}$")  //"^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$")
    private String phone;


    @Builder.Default
    private Long salary= Long.valueOf(-1);

    @Builder.Default
    private Long  companyId=Long.valueOf(-1);

    @Builder.Default
    private Long  departmentId=Long.valueOf(-1);

    @Builder.Default
    private Long managerId= Long.valueOf(-1);

    @Builder.Default
    private String designation=EMPLOYEE;

    public  EmployeeAddRequest(String name,String phone,Long salary,Long companyId,Long departmentId){
        this.name=name;
        this.phone=phone;
        this.salary=salary;
        this.companyId=companyId;
        this.departmentId=departmentId;
    }

}
