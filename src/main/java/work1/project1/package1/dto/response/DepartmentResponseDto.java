package work1.project1.package1.dto.response;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponseDto implements Serializable {

    @Getter @Setter private Long id;
    @Getter @Setter private Long companyId;
    @Getter @Setter private String departmentName;
    @Getter @Setter private Long status;
    @Getter @Setter private String message;
}
