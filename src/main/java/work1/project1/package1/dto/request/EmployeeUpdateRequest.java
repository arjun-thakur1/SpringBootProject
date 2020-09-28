package work1.project1.package1.dto.request;

import lombok.*;
import work1.project1.package1.myenum.MyEnum;

import javax.validation.Valid;
import javax.validation.constraints.*;

import static work1.project1.package1.constants.ApplicationConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeUpdateRequest {

    @NotNull(message = " id must not be null!! ")
    @Positive(message = " id must be positive!! ")
    @Digits(integer =128 ,fraction = 0 , message = " employee id must be Integer value!! ")
    private Double  id;

    @Digits(integer =128 ,fraction = 0 , message = " company id must be Integer value!! ")
    private Double companyId;

    @Digits(integer =128 ,fraction = 0 , message = " department id must be Integer value!! ")
    private Double departmentId;

    @Positive(message = " positive salary required!! ")
    private Double salary;

    @Digits(integer =128 ,fraction = 0 , message = " manager id must be Integer value!! ")
    private Double  managerId;

    private MyEnum designation;
}
