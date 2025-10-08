package cloudFileStorage.cloudfilestorage.service;

import cloudFileStorage.cloudfilestorage.dto.ResourceInfoDto;
import cloudFileStorage.cloudfilestorage.exceptions.ResourceNotFoundException;
import cloudFileStorage.cloudfilestorage.util.PathUtil;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectoryService {

    private final ResourceService resourceService;
    private final MinioClientService minioClientService;
    private final MinioClient minioClient;
    private final PathUtil pathUtil;


    @Value("${minio.root-bucket}")
    private String usersBucket;

    @SneakyThrows
    public List<ResourceInfoDto> getDirectoryInfo(String path) {

        List<ResourceInfoDto> resourceInfoDtoList = new ArrayList<>();

        if (!minioClientService.isDirectoryExist(usersBucket, path)){
            throw new ResourceNotFoundException();
        }

        Iterable<Result<Item>> listObjects = minioClientService.listObjects(usersBucket, path, false);

        for (Result<Item> result : listObjects) {

            String resourceName = result.get().objectName();

            if (pathUtil.isRootDirectory(resourceName) || path.equals(resourceName)) {
                continue;
            }

            resourceInfoDtoList.add(resourceService.getResourceInfo(resourceName));
        }

        return resourceInfoDtoList;
    }

    public ResourceInfoDto createEmptyDirectory(String path) {

        ObjectWriteResponse emptyDirectory = minioClientService.createEmptyDirectory(usersBucket, path);

        return resourceService.getResourceInfo(emptyDirectory.object());
    }

    @SneakyThrows
    public void createUserDirectory(Integer id) {

        String userDirectory = pathUtil.getUserDirectoryName(id);

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(usersBucket)
                        .object(userDirectory)
                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());

    }
}
