package work1.project1.package1.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import work1.project1.package1.myenum.MyEnum;

import javax.validation.Valid;
import javax.validation.constraints.*;

import static work1.project1.package1.constants.ApplicationConstants.EMPLOYEE;
import static work1.project1.package1.constants.ApplicationConstants._NONE;

@Data
@AllArgsConstructor
public class EmployeeAddRequest {


    @NotEmpty(message = " employee name must not be empty!! ")
    @NotNull(message = "employee name cant be null!!")
    private String name;

    @NotNull(message = " phone no must must not be null!!  ")
    @Pattern(regexp = "^[0-9]{10}$",message = " phone no must be of 10 digit!! ")  //"^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$")
    private String phone;

    private Double salary;

    @Digits(integer =128 ,fraction = 0 , message = " company id must be Integer value!! ")
    private Double  companyId;

    @Digits(integer =128 ,fraction = 0 , message = " department id must be Integer value!! ")
    private Double  departmentId;

    @Digits(integer =128 ,fraction = 0 , message = " manager id must be Integer value!! ")
    private Double managerId;

    private MyEnum designation;

    public EmployeeAddRequest(){
    }
    public  EmployeeAddRequest(String name,String phone,Double salary,Double companyId,Double departmentId){
        this.name=name;
        this.phone=phone;
        this.salary=salary;
        this.companyId=companyId;
        this.departmentId=departmentId;
    }
    public  EmployeeAddRequest(String name,String phone,Double salary,Double managerId,MyEnum designation){
        this.name=name;
        this.phone=phone;
        this.salary=salary;
        this.managerId=managerId;
        this.designation=designation;
    }

}
