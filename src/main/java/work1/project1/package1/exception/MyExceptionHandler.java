package work1.project1.package1.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
    public ResponseEntity<Response> myMessage(CustomException e) {
        return  new ResponseEntity<>(new Response(400,e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedUser.class)
    public ResponseEntity<Response> myMessage(UnAuthorizedUser e) {
        return  new ResponseEntity<>(new Response(401,e.getMessage()), HttpStatus.valueOf(401));
    }
    @ExceptionHandler(NotPresentException.class)
    public ResponseEntity<Response> myMessage(NotPresentException e) {
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
    @ExceptionHandler(SuccessException.class)
    public ResponseEntity<Object> myMessage(SuccessException e) {
        return  new ResponseEntity<>(new Response(200,e.getMessage()),HttpStatus.OK);
    }
    @ExceptionHandler(InsufficientDataException.class)
    public ResponseEntity<Object> myMessage(InsufficientDataException e) {
        return  new ResponseEntity<>(new Response(400,e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse( "Validation Failed",
                ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
         return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @Override //long value empty validation
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        ExceptionResponse exceptionResponse = new ExceptionResponse( "Validation Failed!!",
                ex.getLocalizedMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers,
                                                                       HttpStatus status, WebRequest request){

      ExceptionResponse exceptionResponse = new ExceptionResponse( "method not allowed",
              ex.getLocalizedMessage());
      return new ResponseEntity<>(exceptionResponse, HttpStatus.METHOD_NOT_ALLOWED);
  }


}
