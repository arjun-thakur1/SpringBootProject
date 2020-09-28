package work1.project1.package1.exception;

import lombok.*;
import org.springframework.validation.BindingResult;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ExceptionResponse {

        private String timestamp= new Date().toString();
        private String message;
        private String details;

        public ExceptionResponse(String  message,String details){
                this.message=message;
                this.details=details;
        }
}
