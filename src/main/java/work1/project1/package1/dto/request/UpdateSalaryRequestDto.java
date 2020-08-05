package work1.project1.package1.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSalaryRequestDto {

    @Builder.Default
    private Long companyId= Long.valueOf(-1);
    @Builder.Default
    private Long departmentId=Long.valueOf(-1);
    @Builder.Default
    private Long employeeId=Long.valueOf(-1);

    @NotNull
    private  Long salary;
    @NotNull
    private boolean flag;
}
