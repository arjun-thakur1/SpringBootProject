package Work1.Project1.Package.response;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDepartment implements Serializable {
    @Getter @Setter private Long companyId;
    @Getter @Setter private Long departmentId;
    @Getter @Setter private String departmentName;
    @Getter @Setter private Long managerId;
}
