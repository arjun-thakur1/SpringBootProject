package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentUpdateRequest {

    @NotNull
    private Long departmentId;

    @NotNull
    private String departmentName;

}
