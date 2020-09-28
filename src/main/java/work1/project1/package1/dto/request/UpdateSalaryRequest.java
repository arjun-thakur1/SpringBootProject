package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSalaryRequest {

    @NotNull(message = " salary change must not be null!! ")      //@Positive(message = " salary increment must be positive!! ")
    private  Double salary_change;
    @NotNull(message = " must not be null!! ")
    @Digits(integer =128,fraction = 0,message = "type must be integer value!!")
    private Long type;

    private Double companyId;
    private Double departmentId;
    private Double employeeId;

    public UpdateSalaryRequest(double salary_change, Long type, Double employeeId) {
        this.setSalary_change(salary_change);
        this.type=type;
        this.employeeId=employeeId;
    }
}
