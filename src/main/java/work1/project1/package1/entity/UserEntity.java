package work1.project1.package1.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="employeeId")
    private Long employeeId;

    @Column(name="_password")
    private String _password;



    public UserEntity(Long employeeId,String _password) {
        this.employeeId=employeeId;
        this._password=_password;
    }


}
