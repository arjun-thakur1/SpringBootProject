package Work1.Project1.Package.entity;

import lombok.*;


import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
//import javax.persistence.ManyToOne;

import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@Embeddable
@AllArgsConstructor
public class DepartmentPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private Long companyId;

   // @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter private Long departmentId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public DepartmentPK()
    {

    }
    public  DepartmentPK(Long companyId)
    {
        this.companyId=companyId;
    }


}
