package work1.project1.package1.dto.request;

import lombok.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyAddRequest implements Serializable {


    @NotNull(message = " company name must not be null!! ")
    @NotEmpty
    private String companyName;

    @Builder.Default
    private String ceoName=null;
}
