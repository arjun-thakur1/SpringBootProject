package work1.project1.package1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class EmployeeGetResponse implements Serializable {
    private static final long serialVersionUID = 1L;

     private Long   id;
    @Getter @Setter private String name;
    @Getter @Setter private String phone;
    @Getter @Setter private Long status= Long.valueOf(200);
    @Getter @Setter private String message=SUCCESS;

    public EmployeeGetResponse(Long id, String name, String phone) {
        this.id=id;
        this.name=name;
        this.phone=phone;
    }

}
