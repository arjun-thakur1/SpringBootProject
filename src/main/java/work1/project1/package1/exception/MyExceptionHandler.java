package work1.project1.package1.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import work1.project1.package1.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@ControllerAdvice
public class MyExceptionHandler  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> myMessage(CustomException e) {
        return  new ResponseEntity<>(new Response(400,e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotPresentException.class)
    public ResponseEntity<Object> myMessage(NotPresentException e) {
        return  new ResponseEntity<>(new Response(404,e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<Object> myMessage(DuplicateDataException e) {
        return  new ResponseEntity<>(new Response(409,e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResponseHttp.class)
    public ResponseEntity<Object> myMessage(ResponseHttp e) {
        return  new ResponseEntity<>(new Response(404,e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Validation Failed","Please Enter all required filled");
         return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
