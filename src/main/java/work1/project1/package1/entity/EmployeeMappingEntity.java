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
public class EmployeeMappingEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    @GeneratedValue (strategy = GenerationType.AUTO)
    public Long id;

    @Getter @Setter
    @Column(name="company_department_id")
    private Long  mappingId; //company-department mapping table id(PK)

    @Getter @Setter
    @Column(name="employee_id")
    private Long  employeeId;


    @Getter @Setter
    @Column(name="is_active")
    private boolean isActive;

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

    public boolean getIsActive() {
        return this.isActive;
    }



    public EmployeeMappingEntity(Long mappingId, Long employeeId, long createdBy, long updatedBy, boolean isActive){
       // super(createdBy,updatedBy);
        this.mappingId=mappingId;
        this.employeeId = employeeId;
        this.isActive = isActive;
        this.createdBy=createdBy;
        this.updatedBy=updatedBy;
    }
}
