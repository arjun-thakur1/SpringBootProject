package work1.project1.package1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenGenerateResponse {
    private Long status;
    private String accessToken;
}
