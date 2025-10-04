package cloudFileStorage.cloudfilestorage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FileDto {

    String path;
    String name;
    int size;
    FileType fileType;

}
