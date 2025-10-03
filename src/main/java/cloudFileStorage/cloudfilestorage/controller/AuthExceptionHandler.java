package cloudFileStorage.cloudfilestorage.controller;

import cloudFileStorage.cloudfilestorage.exceptions.UserAlreadyExistException;
import cloudFileStorage.cloudfilestorage.util.ErrorResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {


    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponseMessage> userAlreadyExistHandle(UserAlreadyExistException ex){
        log.info("USER ALREADY EXIST");
        return new ResponseEntity<>(new ErrorResponseMessage(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseMessage> nonValidArgumentHandle(MethodArgumentNotValidException ex){
        log.info("INVALID ARGUMENTS");
        return new ResponseEntity<>(new ErrorResponseMessage(ex.getBindingResult().getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseMessage> authExceptionHandle(AuthenticationException ex){
        log.info("FAILED AUTHORIZATION");
        return new ResponseEntity<>(new ErrorResponseMessage("Invalid username or password"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseMessage> unknownExceptionHandle(Exception ex){
        log.warn("UNKNOWN EXCEPTION");
        return new ResponseEntity<>(new ErrorResponseMessage(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
