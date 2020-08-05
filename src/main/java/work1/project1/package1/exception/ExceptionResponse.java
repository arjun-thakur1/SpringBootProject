package work1.project1.package1.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
public class ExceptionResponse {

    @Getter @Setter    private Date   timestamp;
    @Getter @Setter    private String message;
    @Getter @Setter    private String details;


    public ExceptionResponse(Date timestamp, String message, String details) {
       this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
    public ExceptionResponse(Date timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;

    }
}
