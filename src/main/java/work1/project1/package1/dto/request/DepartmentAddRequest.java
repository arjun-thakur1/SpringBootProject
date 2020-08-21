package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentAddRequest {

    @NotNull @NotEmpty
    @Getter @Setter private String departmentName;

}
