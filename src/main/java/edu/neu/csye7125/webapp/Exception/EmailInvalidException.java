package edu.neu.csye7125.webapp.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailInvalidException extends RuntimeException {

    public EmailInvalidException(String message) {super(message);}

}
