package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCompanyAddRequest implements Serializable {

    @NotNull(message = " company ID must not be null!! ")
    @Positive(message = "company id must be positive")
    @Digits(integer =128 ,fraction = 0 , message = " company id must be Integer value!! ")
    private Double companyId;

    @NotNull(message = " department ID must not be null!! ")
    @Positive(message = "department id must be positive")
    @Digits(integer =128 ,fraction = 0 , message = " department id must be Integer value!! ")
    private Double  departmentId;
}
