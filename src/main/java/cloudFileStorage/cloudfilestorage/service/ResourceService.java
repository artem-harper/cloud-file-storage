package cloudFileStorage.cloudfilestorage.service;

import cloudFileStorage.cloudfilestorage.dto.ResourceInfoDto;
import cloudFileStorage.cloudfilestorage.dto.ResourceType;
import cloudFileStorage.cloudfilestorage.exceptions.ResourceNotFoundException;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {


    private final MinioClient minioClient;
    private final ModelMapper modelMapper;

    @Value("${minio.root-bucket}")
    private String usersBucket;

    @SneakyThrows
    public void createUserFolder(Integer id) {

        String userFolderName = "user-%s-files/".formatted(id);

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(usersBucket)
                        .object(userFolderName)
                        .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());

    }

    @SneakyThrows
    public ResourceInfoDto getResourceInfo(String userFolder, String path) {

        char delimetr = '/';
        int lastIndex = path.lastIndexOf(delimetr);
        String fileName = path.substring(lastIndex + 1);
        String filePath = lastIndex == -1 ? "/" : path.substring(0, lastIndex + 1);

        ResourceInfoDto resourceInfoDto = ResourceInfoDto.builder()
                .path(filePath)
                .name(fileName)
                .build();

        try {
            long fileSize = minioClient.statObject(StatObjectArgs.builder()
                            .bucket(usersBucket)
                            .object(userFolder + path)
                            .build())
                    .size();

            resourceInfoDto.setSize(fileSize);
            resourceInfoDto.setResourceType(ResourceType.FILE);

        } catch (ErrorResponseException e) {

            if (!isFolderExist(userFolder, path)) {
                throw new ResourceNotFoundException();
            }

            resourceInfoDto.setResourceType(ResourceType.DIRECTORY);
        }

        return resourceInfoDto;
    }

    @SneakyThrows
    public void deleteResource(String userFolder, String path) {

        List<DeleteObject> list = new ArrayList<>();

        try {
            GetObjectResponse file = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(usersBucket)
                    .object(userFolder + path)
                    .build());

            list.add(new DeleteObject(userFolder + path));
        } catch (ErrorResponseException e) {

            if (!isFolderExist(userFolder, path)) {
                throw new ResourceNotFoundException();
            }

            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(usersBucket)
                    .prefix(userFolder + path + '/')
                    .build());

            for (Result<Item> result : results) {
                list.add(new DeleteObject(result.get().objectName()));
            }

        }

        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder()
                .bucket(usersBucket)
                .objects(list)
                .build());

        for (Result<DeleteError> result : results) {
            DeleteError deleteError = result.get();
        }
    }

    @SneakyThrows
    public InputStream downloadResource(String userFolder, String path) {

        String fileName = path.substring(path.lastIndexOf('/')+1);

        return minioClient.getObject(GetObjectArgs.builder()
                        .bucket(usersBucket)
                        .object(userFolder+path)
                .build());

    }

    public boolean isFolderExist(String userFolder, String path) {

        return minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(usersBucket)
                .prefix(userFolder + path + '/')
                .maxKeys(1)
                .build()).iterator().hasNext();

    }


}
