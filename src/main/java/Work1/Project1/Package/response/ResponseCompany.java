package Work1.Project1.Package.response;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ResponseCompany implements Serializable {


    @Getter @Setter private Long  companyId;
    @Getter @Setter private String companyName;
    @Getter @Setter private String ceoName;
}
