package cloudFileStorage.cloudfilestorage.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(){
        super("Папка или файл не существует");
    }
}
