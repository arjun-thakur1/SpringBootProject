package work1.project1.package1.dto.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {

    @NotNull(message = " id must not be null!! ")
    @Positive(message = " id must be positive!! ")
    private Long id;

    @NotNull(message = " new-password must not be null!! ")
    @NotEmpty(message = " new-password must not be empty!! ")
    private String newPassword;
}
