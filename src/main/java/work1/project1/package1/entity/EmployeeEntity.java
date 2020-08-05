package work1.project1.package1.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "employee")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class EmployeeEntity {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Getter @Setter
    @Column(name="name")
    private String name;

    @Getter @Setter
    @Column(name="phone")
    private String phone;


    @Getter @Setter
    @Column(name="salary")
    private Long salary;

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

    @OneToMany(mappedBy = "employeeId",fetch = FetchType.LAZY)
    private List<DepartmentEmployeeMapping>  departmentEmployeeMappingList;


    public EmployeeEntity(String name, String  phone,Long salary, long createdBy, long updatedBy) {
        this.name = name;
        this.phone = phone;
        this.salary=salary;
        this.createdBy=createdBy;
        this.updatedBy=updatedBy;
    }


}
