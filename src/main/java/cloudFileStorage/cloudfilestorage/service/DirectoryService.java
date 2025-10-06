package cloudFileStorage.cloudfilestorage.service;

import cloudFileStorage.cloudfilestorage.dto.ResourceInfoDto;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectoryService {

    private final ResourceService resourceService;
    private final MinioClientService minioClientService;

    @Value("${minio.root-bucket}")
    private String usersBucket;

    @SneakyThrows
    public List<ResourceInfoDto> getDirectoryInfo(String path) {

        List<ResourceInfoDto> resourceInfoDtoList = new ArrayList<>();

        Iterable<Result<Item>> listObjects = minioClientService.listObjects(usersBucket, path, false);

        for (Result<Item> result : listObjects) {

            String resourceName = result.get().objectName();

            if (resourceName.substring(resourceName.indexOf("/") + 1).isEmpty()) {
                continue;
            }

            resourceInfoDtoList.add(resourceService.getResourceInfo(resourceName));
        }

        return resourceInfoDtoList;
    }
}
