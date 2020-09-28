package work1.project1.package1.dto.response;

import lombok.*;

import java.io.Serializable;

import static work1.project1.package1.constants.ApplicationConstants.SUCCESS;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponse implements Serializable {
    private static final long serialVersionUID = 1L;

     private Long id;
     private String departmentName;
     private String message=SUCCESS;

   public  DepartmentResponse(Long id,String departmentName){
       this.id=id;
       this.departmentName=departmentName;
   }

}
