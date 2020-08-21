package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {

    @NotNull
    @Getter @Setter private Long id;

    @NotNull
    @Getter @Setter private String newPassword;
}
