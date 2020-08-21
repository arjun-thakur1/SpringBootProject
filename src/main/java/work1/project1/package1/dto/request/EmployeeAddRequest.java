package work1.project1.package1.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import work1.project1.package1.myenum.MyEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

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


    @Builder.Default @Positive
    private Long salary= Long.valueOf(1);

    @Builder.Default
    private Long  companyId=Long.valueOf(-1);

    @Builder.Default
    private Long  departmentId=Long.valueOf(-1);

    @Builder.Default
    private Long managerId= Long.valueOf(-1);

    @Builder.Default
    private MyEnum designation= MyEnum.none;

    //@JsonIgnore
    //private
    public  EmployeeAddRequest(String name,String phone,Long salary,Long companyId,Long departmentId){
        this.name=name;
        this.phone=phone;
        this.salary=salary;
        this.companyId=companyId;
        this.departmentId=departmentId;
    }

}
