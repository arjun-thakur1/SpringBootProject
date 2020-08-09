package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static work1.project1.package1.constants.ApplicationConstants.PHONE;
import static work1.project1.package1.constants.ApplicationConstants._NONE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeUpdateRequest {

    @NotNull
    @Getter @Setter private Long  id;

    @Builder.Default
    @Getter @Setter private Long companyId=Long.valueOf(-1);

    @Builder.Default
    @Getter @Setter private Long departmentId=Long.valueOf(-1);

    @Builder.Default
    @Setter @Getter private String name=null;

    @Pattern(regexp = "^[0-9]{10}$")
    @Builder.Default
    private String phone=null;

    @Builder.Default
    @Getter @Setter private Long salary= Long.valueOf(-1);

    @Builder.Default
    @Getter @Setter private Long  managerId= Long.valueOf(-1);

    @Builder.Default
    @Getter @Setter private String designation=null;

}
