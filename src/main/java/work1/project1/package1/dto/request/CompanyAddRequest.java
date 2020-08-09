package work1.project1.package1.dto.request;

import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyAddRequest implements Serializable {


    @NotNull
    @Getter @Setter private String companyName;

    @Builder.Default
    @Getter @Setter private String ceoName=null;

}
