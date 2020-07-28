package Work1.Project1.Package.request;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDepartment implements Serializable {

    @Getter @Setter private Long companyId;
    @Getter @Setter private String departmentName;

}
