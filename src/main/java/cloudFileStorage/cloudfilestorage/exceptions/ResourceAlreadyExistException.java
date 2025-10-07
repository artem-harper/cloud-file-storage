package cloudFileStorage.cloudfilestorage.exceptions;

public class ResourceAlreadyExistException extends RuntimeException{

    public ResourceAlreadyExistException(){
        super("Папка или файл уже существуют по этому пути");
    }

}
