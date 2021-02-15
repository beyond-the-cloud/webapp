package edu.neu.csye7125.webapp.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyExist extends RuntimeException {

    public UserAlreadyExist(String message) {super(message);}

}
