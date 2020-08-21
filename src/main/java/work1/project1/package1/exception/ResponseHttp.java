package work1.project1.package1.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Data
@AllArgsConstructor
@Getter @Setter
public class ResponseHttp extends Throwable {

     private HttpStatus status;
     private String message;
     public  ResponseHttp(){
     }
}
