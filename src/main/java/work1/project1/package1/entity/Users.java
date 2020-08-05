package work1.project1.package1.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name="_password")
    private String _password;



    public Users(String _password)
    {
        this._password=_password;
    }


}
