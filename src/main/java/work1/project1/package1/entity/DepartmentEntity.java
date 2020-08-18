package work1.project1.package1.entity;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import work1.project1.package1.other.CommonFeilds;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentEntity extends CommonFeilds {

    @Id
    @Column(name="id")
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long  id;

    @Getter @Setter
    @Column(name="department_name")
    private String departmentName;

    @Getter @Setter
    @Column(name="created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Getter @Setter
    @Column(name="created_by")
    private Long createdBy;

    @Getter @Setter
    @Column(name="updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Getter @Setter
    @Column(name="updated_by")
    private Long updatedBy;

    public DepartmentEntity(String departmentName,long createdBy,long updatedBy) {
        this.departmentName=departmentName;
        this.createdBy=createdBy;
        this.updatedBy=updatedBy;
    }

}
