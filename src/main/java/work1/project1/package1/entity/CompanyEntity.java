package work1.project1.package1.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@AllArgsConstructor
@Data
@NoArgsConstructor
public class CompanyEntity  {

    @Id
    @Column(name="id")
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long  id;

    @Column(name="company_name")
    private String companyName;

    @Column(name="ceo_name")
    private String ceoName;

    @Column(name="is_active")
    private boolean isActive;

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
    
    public CompanyEntity(String companyName,String  ceoName,long createdBy,long updatedBy, boolean isActive) {
        this.companyName = companyName;
        this.ceoName = ceoName;
        this.isActive = isActive;
        this.createdBy=createdBy;
        this.updatedBy=updatedBy;
    }
    public CompanyEntity(long id, String companyName, String ceoName) {
        this.id=id;
        this.companyName=companyName;
        this.ceoName=ceoName;
    }

    public CompanyEntity(Long companyId, String companyName, String ceoName, boolean b) {
        this.id=companyId;
        this.companyName=companyName;
        this.ceoName=ceoName;
        setActive(b);
    }

    public boolean getIsActive() {

        return this.isActive;
    }

}
