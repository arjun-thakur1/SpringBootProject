package work1.project1.package1.exception;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.BindingResult;

import java.util.Date;
@Data
@NoArgsConstructor
@Getter @Setter
public class ExceptionResponse {

        private Date   timestamp;
        private String message;

        private String details;

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
