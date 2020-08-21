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

    @Column(name="userId")  //basically id is employeeId
    private Long userId;

    @Column(name="_password")
    private String _password;


    public UserEntity(Long userId,String _password) {
        this.userId=userId;
        this._password=_password;
    }

}
