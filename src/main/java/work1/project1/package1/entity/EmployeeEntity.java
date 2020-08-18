package work1.project1.package1.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class EmployeeEntity  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;


    @Column(name="name")
    @Getter @Setter private String name;


    @Column(name="phone")
    @Getter @Setter private String phone;


    @Getter @Setter
    @Column(name="salary")
    private Long salary;

    @Getter @Setter
    @Column(name="manager_id")
    private Long  managerId;


    @Getter @Setter
    @Column(name="designation")
    private String designation;

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

    public EmployeeEntity(String name, String phone, Long salary, Long managerId, String designation, Long createdBy, Long updatedBy) {
     //super(createdBy,updatedBy);
     this.name=name;
     this.phone=phone;
     this.salary=salary;
     this.managerId=managerId;
     this.designation=designation;
     this.createdBy=createdBy;
     this.updatedBy=updatedBy;
    }

    public EmployeeEntity(String name, String  phone,Long salary, long createdBy, long updatedBy) {
       // super(createdBy,updatedBy);
        this.name = name;
        this.phone = phone;
        this.salary=salary;
        this.createdBy=createdBy;
        this.updatedBy=updatedBy;
    }
}
