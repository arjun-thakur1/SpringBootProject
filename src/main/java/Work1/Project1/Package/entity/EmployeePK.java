package Work1.Project1.Package.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class EmployeePK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private Long companyId;
    @Getter @Setter private Long departmentId;
    @Getter @Setter private Long employeeId;




}
