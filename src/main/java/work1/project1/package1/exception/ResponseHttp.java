package work1.project1.package1.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor

public class ResponseHttp extends Throwable {

    @Getter @Setter private HttpStatus status;
    @Getter @Setter private String message;
    public  ResponseHttp(){
    }
}
