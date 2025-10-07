package cloudFileStorage.cloudfilestorage.util;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PathUtil {

    private final String SEPARATOR = "/";

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

    public String formatPath(String path){

        String formatedPath=path;

        if (path.endsWith(SEPARATOR)) {
            formatedPath = path.substring(0, path.lastIndexOf(SEPARATOR));
        }
        return formatedPath;
    }

    public String createUserDirectory(int id){

        return "user-%s-files/".formatted(id);
    }

    public boolean isRootDirectory(String path){
        if(path.substring(path.indexOf(SEPARATOR) + 1).isEmpty()){
            return true;
        }
        return false;
    }
}
