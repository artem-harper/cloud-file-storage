package cloudFileStorage.cloudfilestorage.controller.utilControllers;

import cloudFileStorage.cloudfilestorage.exceptions.ResourceAlreadyExistException;
import cloudFileStorage.cloudfilestorage.exceptions.ResourceNotFoundException;
import cloudFileStorage.cloudfilestorage.util.ErrorResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ResourceExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseMessage> resourceNotExistHandle(ResourceNotFoundException ex){
        log.warn("RESOURCE NOT FOUND");
        return new ResponseEntity<>(new ErrorResponseMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ErrorResponseMessage> resourceAlreadyExistHandle(ResourceAlreadyExistException ex){
        log.warn("RESOURCE ALREADY EXIST");
        return new ResponseEntity<>(new ErrorResponseMessage(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseMessage> unknownExceptionHandle(Exception ex){
        log.warn("UNKNOWN EXCEPTION");
        return new ResponseEntity<>(new ErrorResponseMessage(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
