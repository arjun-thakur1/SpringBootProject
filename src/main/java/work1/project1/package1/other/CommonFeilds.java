package work1.project1.package1.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
public class  CommonFeilds {

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

    public CommonFeilds(Long createdBy,Long updatedBy)
    {
        this.createdBy=createdBy;
        this.updatedBy=updatedBy;
    }
}
