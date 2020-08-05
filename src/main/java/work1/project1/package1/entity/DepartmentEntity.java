package work1.project1.package1.entity;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table
//@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long  id;

    @Getter @Setter
    @Column(name="department_name")
    private String departmentName;

    @Getter @Setter
    @Column(name="company_id")
    private Long  companyId;

    @Getter
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

    @Getter @Setter @Column(name="is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "departmentId",fetch = FetchType.LAZY)
    private List<DepartmentEmployeeMapping> departmentEmployeeMappingList;

    public boolean getIsActive() {

        return this.isActive;
    }
    public DepartmentEntity(String departmentName,Long companyId,long createdBy,long updatedBy, boolean isActive) {
        this.departmentName=departmentName;
        this.companyId=companyId;
        this.createdBy=createdBy;
        this.updatedBy=updatedBy;
        this.isActive = isActive;
    }
    public DepartmentEntity(Long departmentId,String departmentName,Long companyId,long createdBy,long updatedBy,
                            boolean isActive) {
        this.id=departmentId;
        this.departmentName=departmentName;
        this.companyId=companyId;
        this.createdBy=createdBy;
        this.updatedBy=updatedBy;
        this.isActive = isActive;
    }
}
