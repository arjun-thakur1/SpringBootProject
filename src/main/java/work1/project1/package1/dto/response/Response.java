package work1.project1.package1.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Response implements Serializable {

    @Getter @Setter private long status;
    @Getter @Setter private String message;
    public Response(){
    }

}
