package work1.project1.package1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;

@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponse {

    @Getter @Setter private Long id;
    @Getter @Setter private String departmentName;
    @Getter @Setter private String message=SUCCESS;


}
