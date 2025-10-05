package cloudFileStorage.cloudfilestorage.controller.utilControllers;

import cloudFileStorage.cloudfilestorage.exceptions.ResourceNotFoundException;
import cloudFileStorage.cloudfilestorage.util.ErrorResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseMessage> resourceNotExistHandle(ResourceNotFoundException ex){
        return new ResponseEntity<>(new ErrorResponseMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
