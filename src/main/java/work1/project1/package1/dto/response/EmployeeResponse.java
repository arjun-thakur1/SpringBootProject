package work1.project1.package1.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {

    @Getter @Setter private Long   id;
    @Getter @Setter private String name;
    @Getter @Setter private String phone;


    @Getter @Setter private Long salary;
    @Getter @Setter private Long managerId;
    @Getter @Setter private String designation;

}
