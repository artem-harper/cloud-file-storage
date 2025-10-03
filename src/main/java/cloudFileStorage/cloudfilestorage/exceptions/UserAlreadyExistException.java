package cloudFileStorage.cloudfilestorage.exceptions;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(){
        super("Username already exist");
    }
}
