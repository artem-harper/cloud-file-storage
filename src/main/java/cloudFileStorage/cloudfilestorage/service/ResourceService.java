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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    public ResourceInfoDto getResourceInfo(String path) {


        if (path.endsWith("/")) {
            path = path.substring(0, path.lastIndexOf("/"));
        }

        char delimetr = '/';
        int lastIndex = path.lastIndexOf(delimetr);
        String resourceName = path.substring(lastIndex + 1);
        String resourcePath = path.substring(path.indexOf(delimetr) + 1, path.lastIndexOf(delimetr) + 1);

        if (resourcePath.isEmpty()) {
            resourcePath = "/";
        }

        ResourceInfoDto resourceInfoDto = ResourceInfoDto.builder()
                .path(resourcePath)
                .name(resourceName)
                .build();

        try {
            long fileSize = minioClient.statObject(StatObjectArgs.builder()
                            .bucket(usersBucket)
                            .object(path)
                            .build())
                    .size();

            resourceInfoDto.setSize(fileSize);
            resourceInfoDto.setType(ResourceType.FILE);

        } catch (ErrorResponseException e) {

            if (!isFolderExist(path)) {
                throw new ResourceNotFoundException();
            }

            resourceInfoDto.setName(resourceName + delimetr);
            resourceInfoDto.setType(ResourceType.DIRECTORY);
        }

        return resourceInfoDto;
    }

    @SneakyThrows
    public void deleteResource(String path) {


        List<DeleteObject> list = new ArrayList<>();

        try {
            GetObjectResponse file = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(usersBucket)
                    .object(path)
                    .build());

            list.add(new DeleteObject(path));
        } catch (ErrorResponseException e) {

            if (!isFolderExist(path)) {
                throw new ResourceNotFoundException();
            }


            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(usersBucket)
                    .prefix(path)
                    .build());

            for (Result<Item> result : results) {
                Item item = result.get();
                String itemName = item.objectName();
                list.add(new DeleteObject(itemName));
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
    public byte[] downloadResource(String path) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        try {

            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(usersBucket)
                    .object(path)
                    .build()).readAllBytes();

        } catch (ErrorResponseException e) {

            if (!isFolderExist(path)) {
                throw new ResourceNotFoundException();
            }

            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(usersBucket)
                    .prefix(path)
                    .recursive(true)
                    .build());


            for (Result<Item> result : results) {
                Item item = result.get();
                String resourceName = item.objectName();

                String entryName = resourceName.substring(path.length());
                zos.putNextEntry(new ZipEntry(entryName));

                try (InputStream is = minioClient.getObject(
                        GetObjectArgs.builder().bucket(usersBucket).object(resourceName).build())) {
                    is.transferTo(zos);
                }

                zos.closeEntry();



            }
            zos.finish();
        }

        return baos.toByteArray();
    }

    public boolean isFolderExist(String path) {

        return minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(usersBucket)
                .prefix(path)
                .maxKeys(1)
                .build()).iterator().hasNext();

    }


}
