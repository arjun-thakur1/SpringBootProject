package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentAddRequest {


    @NotEmpty(message = " department name must not be empty!! ")
    @NotNull(message = " department name must not be null!! ")
    private String departmentName;

}
