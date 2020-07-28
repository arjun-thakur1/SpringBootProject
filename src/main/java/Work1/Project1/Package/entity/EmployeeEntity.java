package Work1.Project1.Package.entity;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    public EmployeePK employeePK;

    @Getter @Setter
    @Column(name="empName")
    private String empName;

    @Getter @Setter
    @Column(name="phone")
    private String phone;

    @Getter @Setter
    @Column(name="salary",nullable = false)
    private Long salary;

    @Getter @Setter
    @Column(name="is_active")
    private boolean isActive;

    public boolean getIsActive() {
        return this.isActive;
    }

    // @Getter @Setter
    //@Column(name="manager_id")
    //private long managerId;

}
