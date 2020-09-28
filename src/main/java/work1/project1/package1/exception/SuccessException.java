package work1.project1.package1.exception;

import lombok.*;
import org.springframework.http.HttpStatus;
@Data
@AllArgsConstructor
@Getter @Setter
public class SuccessException extends  Throwable{

   // private HttpStatus status;
   // private String message;
    public SuccessException(String message){
        super(message);
    }
}
