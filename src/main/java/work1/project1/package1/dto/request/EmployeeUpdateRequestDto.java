package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeUpdateRequestDto {

    @NotNull
    @Getter @Setter private Long  id;

    @Builder.Default
    @Getter @Setter private Long departmentId=Long.valueOf(-1);


    @Builder.Default
    @Getter @Setter private Long salary= Long.valueOf(-1);


    @Builder.Default
    @Getter @Setter private Long  managerId= Long.valueOf(-1);

    @Builder.Default
    @Getter @Setter private String designation="NOT_CHANGE";

}
