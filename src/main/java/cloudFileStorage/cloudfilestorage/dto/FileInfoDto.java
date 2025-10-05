package cloudFileStorage.cloudfilestorage.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FileInfoDto {

    String path;
    String name;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    long size;
    FileType fileType;

}
