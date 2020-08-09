package work1.project1.package1.dto.response;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDeleteResponse {

        @Getter @Setter private Long companyId;
        @Getter @Setter private String message;


}
