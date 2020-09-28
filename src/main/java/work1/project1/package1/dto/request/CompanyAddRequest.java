package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAddRequest implements Serializable {

    @NotEmpty(message= " company name must not be empty.Please provide company name!! ")
    @NotNull(message = " company name must not be null!! ")
    private String companyName;


    private String ceoName;
}
