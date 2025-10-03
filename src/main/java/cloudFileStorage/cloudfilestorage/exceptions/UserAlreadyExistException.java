package cloudFileStorage.cloudfilestorage.exceptions;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(){
        super("Пользователь с таким логином уже существует");
    }
}
