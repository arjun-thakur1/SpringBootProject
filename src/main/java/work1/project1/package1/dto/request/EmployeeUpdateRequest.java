package work1.project1.package1.dto.request;

import lombok.*;
import work1.project1.package1.myenum.MyEnum;

import javax.validation.constraints.*;

import static work1.project1.package1.constants.ApplicationConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeUpdateRequest {

    @NotNull @Min(1)
    @Getter @Setter private Long  id;

    @Builder.Default
    @Getter @Setter private Long companyId=Long.valueOf(-1);

    @Builder.Default
    @Getter @Setter private Long departmentId=Long.valueOf(-1);

    @Builder.Default @Positive
    @Getter @Setter private Long salary= Long.valueOf(1);

    @Builder.Default //@Positive
    @Getter @Setter private Long  managerId= Long.valueOf(-1);

    @Builder.Default
    @Getter @Setter private MyEnum designation=MyEnum.employee; //EMPLOYEE

}
