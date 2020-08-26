package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
@Data
@AllArgsConstructor
public class EmployeePersonalInfoUpdateRequest {

    @NotNull //@NotEmpty
    private  Long id;

    @Builder.Default @Setter @Getter
    @NotEmpty
    private String name=null;

    @Pattern(regexp = "^[0-9]{10}$")
    @Builder.Default
    private String phone=null;

}
