package work1.project1.package1.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import work1.project1.package1.myenum.MyEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee")
@AllArgsConstructor
@Data
@Getter @Setter
@NoArgsConstructor
public class EmployeeEntity  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;


    @Column(name="name")
    private String name;

    @Column(name="phone")
    private String phone;

    @Column(name="salary")
    private Double salary;

    @Column(name="manager_id")
    private Long  managerId;

    @Column(name="designation")
    @Enumerated(EnumType.STRING)
    private MyEnum designation;


    @Column(name="password")
    private String password;


    @Column(name="created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="created_by")
    private Long createdBy;

    @Column(name="updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name="updated_by")
    private Long updatedBy;

    public EmployeeEntity(String name, String phone, Double salary, Long managerId, MyEnum designation, Long createdBy,
                          Long updatedBy,String password) {
     //super(createdBy,updatedBy);
     this.name=name;
     this.phone=phone;
     this.salary=salary;
     this.managerId=managerId;
     this.designation=designation;
     this.createdBy=createdBy;
     this.updatedBy=updatedBy;
     this.password=password;
    }

    public EmployeeEntity(String name, String  phone,Double salary, long createdBy, long updatedBy) {
       // super(createdBy,updatedBy);
        this.name = name;
        this.phone = phone;
        this.salary=salary;
        this.createdBy=createdBy;
        this.updatedBy=updatedBy;
    }
    public EmployeeEntity(Long id,String name, String  phone,Double salary,Long managerId,MyEnum designation){
        this.id=id;
        this.name=name;
        this.phone=phone;
        this.salary=salary;
        this.managerId=managerId;
        this.designation=designation;
    }
}
