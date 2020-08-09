package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSalaryRequestDto {

    @NotNull @Getter @Setter
    private  Long salary_increment;

    @NotNull @Getter @Setter
    private Long flag;

    @Builder.Default
    private Long companyId= Long.valueOf(-1);
    @Builder.Default
    private Long departmentId=Long.valueOf(-1);
    @Builder.Default
    private Long employeeId=Long.valueOf(-1);


}
