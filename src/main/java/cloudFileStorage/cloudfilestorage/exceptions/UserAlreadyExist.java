package cloudFileStorage.cloudfilestorage.exceptions;

public class UserAlreadyExist extends RuntimeException{
    public UserAlreadyExist(){
        super("user already exist");
    }
}
