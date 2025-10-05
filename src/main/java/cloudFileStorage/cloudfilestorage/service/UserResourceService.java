package cloudFileStorage.cloudfilestorage.service;

import cloudFileStorage.cloudfilestorage.dto.ResourceInfoDto;
import cloudFileStorage.cloudfilestorage.dto.ResourceType;
import cloudFileStorage.cloudfilestorage.exceptions.ResourceNotFoundException;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class UserResourceService {


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

            boolean isFolderExist = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(usersBucket)
                    .prefix(userFolder + path + delimetr)
                    .maxKeys(1)
                    .build()).iterator().hasNext();

            if (!isFolderExist) {
                throw new ResourceNotFoundException();
            }

            resourceInfoDto.setResourceType(ResourceType.DIRECTORY);
        }

        return resourceInfoDto;
    }

    @SneakyThrows
    public void deleteResource(String userFolder, String path) {

        try {
            minioClient.getObject(GetObjectArgs.builder()
                    .bucket(usersBucket)
                    .object(userFolder + path)
                    .build());
        } catch (ErrorResponseException e) {
            throw new ResourceNotFoundException();
        }


        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(usersBucket)
                .object(userFolder + path)

                .build());
    }

    public void getFolderInfo(String path) {
    }


}
