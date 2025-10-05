package cloudFileStorage.cloudfilestorage.exceptions;

public class ResourceNotExistException extends RuntimeException {

    public ResourceNotExistException(){
        super("Папки или файл не существуют");
    }
}
