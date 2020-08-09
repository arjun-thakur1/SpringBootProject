package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentAddRequest implements Serializable {

    @Builder.Default
    @Getter @Setter private Long companyId= Long.valueOf(-1);

    @Builder.Default
    @Getter @Setter private String departmentName=null;

    @Builder.Default
    @Getter @Setter private Long  departmentId= Long.valueOf(-1);

}
