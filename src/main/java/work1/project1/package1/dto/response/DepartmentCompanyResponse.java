package work1.project1.package1.dto.response;

import lombok.*;

import java.io.Serializable;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCompanyResponse implements Serializable {

    @Getter @Setter private Long companyId;
    @Getter @Setter private Long departmentId;

    @Getter @Setter private String departmentName;
    @Getter @Setter private String message=SUCCESS;
}
