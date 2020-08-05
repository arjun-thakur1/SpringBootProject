package work1.project1.package1.dto.response;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class CompanyResponseDto implements Serializable {


    @Getter @Setter private Long   id;
    @Getter @Setter private String companyName;
    @Getter @Setter private String ceoName;
    @Getter @Setter private Long status;
    @Getter @Setter private String message;

    public CompanyResponseDto(Long companyId, String companyName, String ceoName) {

    }


}
