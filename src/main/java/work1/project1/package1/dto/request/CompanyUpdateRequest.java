package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyUpdateRequest {

    @NotNull
    @Getter @Setter private Long  id;

    @NotEmpty
    @Getter @Setter private String companyName;

    @Getter @Setter private String ceoName;

}
