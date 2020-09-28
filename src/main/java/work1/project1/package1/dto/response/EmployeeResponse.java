package work1.project1.package1.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {

     private Long   id;
     private String name;
     private String phone;


     private double salary;
     private Long managerId;
     private String designation;

}
