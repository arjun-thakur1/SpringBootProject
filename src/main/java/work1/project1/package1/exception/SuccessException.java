package work1.project1.package1.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class SuccessException extends  Throwable{

    private HttpStatus status;
    private String message;

}
