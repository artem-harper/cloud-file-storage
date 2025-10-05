package cloudFileStorage.cloudfilestorage.controller.utilControllers;

import cloudFileStorage.cloudfilestorage.exceptions.ResourceNotExistException;
import cloudFileStorage.cloudfilestorage.util.ErrorResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FileExceptionHandler {

    @ExceptionHandler(ResourceNotExistException.class)
    public ResponseEntity<ErrorResponseMessage> resourceNotExistHandle(ResourceNotExistException ex){
        return new ResponseEntity<>(new ErrorResponseMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
