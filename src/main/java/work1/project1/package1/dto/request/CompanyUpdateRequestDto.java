package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyUpdateRequestDto {

    @NotNull
    @Getter @Setter private Long  id;


    @Getter @Setter private String companyName;

    @Builder.Default
    @Getter @Setter private String ceoName=null;

}
