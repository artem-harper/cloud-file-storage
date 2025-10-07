package cloudFileStorage.cloudfilestorage.controller.utilControllers;

import cloudFileStorage.cloudfilestorage.exceptions.ResourceNotFoundException;
import cloudFileStorage.cloudfilestorage.exceptions.UserAlreadyExistException;
import cloudFileStorage.cloudfilestorage.util.ErrorResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
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
        return new ResponseEntity<>(new ErrorResponseMessage("Неправильный логин или пароль"), HttpStatus.UNAUTHORIZED);
    }

}
