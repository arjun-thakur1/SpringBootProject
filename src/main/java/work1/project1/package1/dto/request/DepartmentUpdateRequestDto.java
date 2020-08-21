package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentUpdateRequestDto {

    @NotNull
    @Getter @Setter private Long departmentId;

    @NotNull
    @Getter @Setter private String departmentName;

}
