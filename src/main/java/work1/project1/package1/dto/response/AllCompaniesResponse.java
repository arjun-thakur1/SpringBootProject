package work1.project1.package1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllCompaniesResponse {

    private Long   id;
    private String companyName;
    private String ceoName;
}
