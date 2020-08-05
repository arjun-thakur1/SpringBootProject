package work1.project1.package1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {


    @Getter @Setter private Long   id;
    @Getter @Setter private String name;
    @Getter @Setter private String phone;
    @Getter @Setter private Long status;
    @Getter @Setter private String message;

    public EmployeeResponseDto(Long id, String name, String phone) {
    }

}
