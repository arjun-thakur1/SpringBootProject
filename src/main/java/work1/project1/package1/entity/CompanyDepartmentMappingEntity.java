package work1.project1.package1.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@AllArgsConstructor
@Data
@Getter @Setter
@NoArgsConstructor
public class CompanyDepartmentMappingEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long  id;

    @Column(name="company_id")
    private Long companyId;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "is_active")
    private boolean isActive;


    @Column(name="created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;


    @Column(name="created_by")
    private Long createdBy;


    @Column(name="updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Getter @Setter
    @Column(name="updated_by")
    private Long updatedBy;


    public CompanyDepartmentMappingEntity(Long companyId, Long departmentId,boolean isActive){
        this.companyId=companyId;
        this.departmentId=departmentId;
        this.isActive=isActive;
    }
    public CompanyDepartmentMappingEntity(Long companyId, Long departmentId,Long createdBy,Long updatedBy,boolean isActive){
       // super(createdBy,updatedBy);
        this.createdBy=createdBy;
        this.updatedBy=updatedBy;
        this.companyId=companyId;
        this.departmentId=departmentId;
        this.isActive=isActive;
    }

}
