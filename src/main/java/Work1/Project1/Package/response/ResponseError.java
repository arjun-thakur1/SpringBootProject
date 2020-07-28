package Work1.Project1.Package.response;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseError implements Serializable {

    @Getter @Setter private long status;
    @Getter @Setter private String message;
}
