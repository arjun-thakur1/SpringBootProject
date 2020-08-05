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
public class CompanyEntity {

    @Id
    @Column(name="id")
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long  id;

    @Column(name="company_name")
    private String companyName;

    @Column(name="ceo_name")
    private String ceoName;

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

    public CompanyEntity(Long companyId,String companyName,String  ceoName,long createdBy,long updatedBy, boolean isActive) {
        this.id=companyId;
        this.companyName = companyName;
        this.ceoName = ceoName;
        this.createdBy=createdBy;
        this.updatedBy=updatedBy;
        this.isActive = isActive;
    }
    public CompanyEntity(String companyName,String  ceoName,long createdBy,long updatedBy, boolean isActive) {
        this.companyName = companyName;
        this.ceoName = ceoName;
        this.createdBy=createdBy;
        this.updatedBy=updatedBy;
        this.isActive = isActive;
    }
}
