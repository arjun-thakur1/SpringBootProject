package work1.project1.package1.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response implements Serializable {

     private long status;
     private String message;
}
