package Work1.Project1.Package.entity;
import lombok.*;

import org.springframework.context.annotation.Configuration;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table
@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
     @EmbeddedId
     @Getter @Setter public DepartmentPK departmentPK;

    @Getter @Setter
    @Column(name="department_name")
    private String departmentName;

    @Getter @Setter @Column(name="is_active")
    private boolean isActive;

    @Getter @Setter @Column(name="manager_id")
    private long managerId;

    public boolean getIsActive() {
        return this.isActive;
    }
}
