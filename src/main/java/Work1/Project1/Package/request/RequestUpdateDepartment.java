package Work1.Project1.Package.request;

import lombok.*;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateDepartment implements Serializable {


        @Getter @Setter private Long companyId;
        @Getter @Setter private Long departmentId;
        @Getter @Setter private String departmentName;
        @Getter @Setter private Long managerId;

}
