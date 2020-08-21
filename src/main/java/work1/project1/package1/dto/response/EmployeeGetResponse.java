package work1.project1.package1.dto.response;

import lombok.*;

import java.io.Serializable;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class EmployeeGetResponse implements Serializable {
    private static final long serialVersionUID = 1L;

     private Long   id;
     private String name;
     private String phone;
     private Long status= Long.valueOf(200);
     private String message=SUCCESS;

    public EmployeeGetResponse(Long id, String name, String phone) {
        this.id=id;
        this.name=name;
        this.phone=phone;
    }

}
