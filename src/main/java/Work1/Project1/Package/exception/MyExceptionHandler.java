package Work1.Project1.Package.exception;

import Work1.Project1.Package.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> myMessage(CustomException e)
    {
        return  new ResponseEntity<>(new Response(404,e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
