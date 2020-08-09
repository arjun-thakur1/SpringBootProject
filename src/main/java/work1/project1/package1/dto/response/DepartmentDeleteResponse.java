package work1.project1.package1.dto.response;

import lombok.*;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDeleteResponse {

    @Getter @Setter private Long departmentId;
    @Getter @Setter private Long companyId;
    @Getter @Setter private String message;

}
