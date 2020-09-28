package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentUpdateRequest {

    @NotNull(message = " department Id must not be null!! ")
    @Positive(message = "department id must be positive")
    @Digits(integer =128 ,fraction = 0 , message = " id must be Integer value!! ")
    private Double departmentId;

    @NotNull(message = " department Name must not be null!! ")
    @NotEmpty(message = " department Name must not be empty!! ")
    private String departmentName;

}
