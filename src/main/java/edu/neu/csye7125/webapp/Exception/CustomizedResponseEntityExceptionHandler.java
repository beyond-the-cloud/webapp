package edu.neu.csye7125.webapp.Exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;


@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    /**
     * "Catch-All" Exception Handler
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(
                        new Date(),
                        ex.getMessage(),
                        request.getDescription(false)
                );
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * User Not Found Exception Handler
     * Throw exception when input user is not found
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(
                        new Date(),
                        "User Not Found!",
                        request.getDescription(false)
                );
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * User Already Exist Exception Handler
     * Throw exception when created user is already exist
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(UserAlreadyExist.class)
    public final ResponseEntity<Object> handleUserAlreadyExist(UserAlreadyExist ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(
                        new Date(),
                        "User Already Exist!",
                        request.getDescription(false)
                );
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Email Invalid Exception Handler
     * Throw exception when user try to modify restricted data fields
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(EmailInvalidException.class)
    public final ResponseEntity<Object> handleEmailInvalidException(EmailInvalidException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(
                        new Date(),
                        "Invalid Email!",
                        request.getDescription(false)
                );
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Fields Restricted Exception Handler
     * Throw exception when user try to modify restricted data fields
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(FieldRestrictedException.class)
    public final ResponseEntity<Object> handleFieldRestrictedException(FieldRestrictedException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(
                        new Date(),
                        "Fields not allowed to modify; Can only change FirstName, LastName and Password",
                        request.getDescription(false)
                );
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Weak Password Exception Handler
     * Throw exception when user try to set a weak password
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(WeakPasswordException.class)
    public final ResponseEntity<Object> handleWeakPasswordException(WeakPasswordException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(
                        new Date(),
                        "Password Too Weak!",
                        request.getDescription(false)
                );
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Unauthorized Exception Handler
     * Throw exception when user is not authorized to access
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<Object> handleAttachFileException(UnauthorizedException ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(
                        new Date(),
                        "This user is unauthorized to access this resource!",
                        request.getDescription(false)
                );
        return new ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(
                        new Date(),
                        "Validation Failed",
                        ex.getBindingResult().toString()
                );
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
