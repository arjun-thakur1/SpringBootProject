package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSalaryRequest {

    @NotNull //@Positive(message = " salary increment must be positive!! ")
    private  Long salary_increment;
    @NotNull
    private Long flag;
    @Builder.Default
    private Long companyId= Long.valueOf(-1);
    @Builder.Default
    private Long departmentId=Long.valueOf(-1);
    @Builder.Default
    private Long employeeId=Long.valueOf(-1);

}
