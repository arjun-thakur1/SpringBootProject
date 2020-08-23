package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {

    @NotNull @Positive(message = "id must be positive!!")
    @Getter @Setter private Long id;

    @NotNull
    @Getter @Setter private String newPassword;
}
