package Work1.Project1.Package.response;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Response implements Serializable {

    @Getter @Setter private long status;
    @Getter @Setter private String message;
    public Response(){

    }
}
