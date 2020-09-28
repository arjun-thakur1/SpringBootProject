package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
@Data
@AllArgsConstructor
public class EmployeePersonalInfoUpdateRequest {

    @NotNull(message = " employee id must not be null!! ") //@NotEmpty
    @Digits(integer =128 ,fraction = 0,message = " id must be Integer value ")
    private  Double id;

    //@NotEmpty
    private String name;

    @Pattern(regexp = "^[0-9]{10}$",message = " phone no must be of 10 digits!! ")
    private String phone;

}
