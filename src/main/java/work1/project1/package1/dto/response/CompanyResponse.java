package work1.project1.package1.dto.response;

import lombok.*;

import java.io.Serializable;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class CompanyResponse implements Serializable {


    private Long   id;
    private String companyName;
    private String ceoName;
    private String message= SUCCESS;

    public CompanyResponse(Long companyId, String companyName, String ceoName) {
        this.id=id;
        this.ceoName=ceoName;
        this.companyName=companyName;
    }


}
