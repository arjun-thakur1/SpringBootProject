package Work1.Project1.Package.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CompanyEntity {

    @Id
    @Column(name="company_id")
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long  companyId;

    @Column(name="company_name")
    private String companyName;

    @Column(name="ceo_name")
    private String ceoName;

    @Getter @Setter
    @Column(name="is_active")
     private boolean isActive;

    public boolean getIsActive()
    {
        return this.isActive;
    }


    public CompanyEntity(String companyName, String ceoName, boolean isActive) {
        this.companyName = companyName;
        this.ceoName = ceoName;
        this.isActive = isActive;
    }


}
