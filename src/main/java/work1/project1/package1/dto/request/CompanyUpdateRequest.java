package work1.project1.package1.dto.request;

import com.sun.org.glassfish.gmbal.NameValue;
import lombok.*;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyUpdateRequest {

    @NotNull(message= " please provide company id!! ")
    @Positive(message = "id must be positive")
    @Digits(integer =128 ,fraction = 0 , message = " id must be Integer value!! ")
    private Double  id;

    String companyName;
    String ceoName;

}
