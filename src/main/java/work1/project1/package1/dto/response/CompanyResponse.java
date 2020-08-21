package work1.project1.package1.dto.response;

import lombok.*;

import java.io.Serializable;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class CompanyResponse implements Serializable {


    @Getter @Setter private Long   id;
    @Getter @Setter private String companyName;
    @Getter @Setter private String ceoName;
    @Getter @Setter private String message= SUCCESS;

    public CompanyResponse(Long companyId, String companyName, String ceoName) {
    }


}
