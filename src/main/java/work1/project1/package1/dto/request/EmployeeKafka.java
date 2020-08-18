package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.io.Serializable;

import static work1.project1.package1.constants.ApplicationConstants.EMPLOYEE;
import static work1.project1.package1.constants.ApplicationConstants._NONE;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeKafka implements Serializable {

    private static final long serialVersionUID = 1L;

    @Builder.Default
    private Long companyId=-1L;

    @Builder.Default
    private Long departmentId=-1L;

    @NotNull @Positive
    private Long salary;

    private String name;

    @Pattern(regexp = "^[0-9]{10}$")
    private String phone;

    @Builder.Default
    private Long employeeId=-1L;

    @Builder.Default
    private Long managerId= -1L;

    @Builder.Default
    private String designation=EMPLOYEE;
}

