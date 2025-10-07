package cloudFileStorage.cloudfilestorage.util;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PathUtil {

    private final String SEPARATOR = "/";
    private final String USER_DIRECTORY = "user-%s-files/";

    public String getResourceNameFromPath(String path){

        int lastIndex = path.lastIndexOf(SEPARATOR);
        return path.substring(lastIndex + 1);
    }

    public String getResourcePath(String path){

        String resourcePath = path.substring(path.indexOf(SEPARATOR) + 1, path.lastIndexOf(SEPARATOR) + 1);

        if (resourcePath.isEmpty()) {
            resourcePath = SEPARATOR;
        }

        return resourcePath;
    }

    public String removeLastSlash(String path){

        String formatedPath=path;

        if (path.endsWith(SEPARATOR)) {
            formatedPath = path.substring(0, path.lastIndexOf(SEPARATOR));
        }
        return formatedPath;
    }

    public String removeFirstSlash(String path){
        if (path.startsWith("/")) {
            path = path.substring(path.indexOf("/") + 1);
        }
        return path;
    }

    public String getUserDirectoryName(int id){
        return "user-%s-files/".formatted(id);
    }

    public boolean isRootDirectory(String path){
        if(path.substring(path.indexOf(SEPARATOR) + 1).isEmpty()){
            return true;
        }
        return false;
    }
}
