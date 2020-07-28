package Work1.Project1.Package.response;

import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ResponseCompany {


    @Getter @Setter private Long  companyId;
    @Getter @Setter private String companyName;
    @Getter @Setter private String ceoName;
}
