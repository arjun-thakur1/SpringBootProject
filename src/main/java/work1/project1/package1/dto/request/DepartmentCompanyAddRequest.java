package work1.project1.package1.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentCompanyAddRequest implements Serializable {

    @NotNull
    @Getter @Setter private Long companyId;

    @NotNull
    @Getter @Setter private Long  departmentId;
}
