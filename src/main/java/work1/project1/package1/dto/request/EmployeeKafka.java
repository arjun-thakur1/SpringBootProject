package work1.project1.package1.dto.request;

import lombok.*;
import work1.project1.package1.myenum.MyEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.io.Serializable;

import static work1.project1.package1.constants.ApplicationConstants.EMPLOYEE;
import static work1.project1.package1.constants.ApplicationConstants._NONE;
@Data
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeKafka implements Serializable {

    private static final long serialVersionUID = 18288380103813L;

    private Long companyId;
    private Long departmentId;

    private Double salary;

    private String name;

    @Pattern(regexp = "^[0-9]{10}$")
    private String phone;

    private Long employeeId;

    private Long managerId;

    private MyEnum designation;//=MyEnum.none;

}

