package work1.project1.package1.entity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "department_employee_mapping")
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentEmployeeMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    @GeneratedValue (strategy = GenerationType.AUTO)
    public Long id;

    @Getter @Setter
    @Column(name="department_id")
    private Long  departmentId;

    @Getter @Setter
    @Column(name="employee_id")
    private Long  employeeId;

    @Getter @Setter
    @Column(name="manager_id")
    private Long  managerId;


    @Getter @Setter
    @Column(name="designation")
    private String designation;


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

    @Getter @Setter
    @Column(name="is_active")
    private boolean isActive;
    public boolean getIsActive() {
        return this.isActive;
    }



    public DepartmentEmployeeMapping(Long departmentId, Long employeeId, Long managerId, String designation, long createdBy, long
            updatedBy, boolean isActive){
        this.departmentId=departmentId;
        this.employeeId = employeeId;
        this.managerId = managerId;
        this.designation=designation;
        this.createdBy=createdBy;
        this.updatedBy=updatedBy;
        this.isActive = isActive;
    }
}
