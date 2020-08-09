package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyUpdateRequest {

    @NotNull
    @Getter @Setter private Long  id;


    @Getter @Setter private String companyName;


    @Getter @Setter private String ceoName;

}
