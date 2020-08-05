package work1.project1.package1.dto.response;

import lombok.Getter;
import lombok.Setter;

public class EmployeeCompleteResponseDto {

    @Getter @Setter private Long   id;
    @Getter @Setter private String name;
    @Getter @Setter private String phone;
    @Getter @Setter private Long salary;
    @Getter @Setter private Long managerId;
    @Getter @Setter private String designation;

    @Getter @Setter private Long status;
    @Getter @Setter private String message;

}
