package cloudFileStorage.cloudfilestorage.service;

import cloudFileStorage.cloudfilestorage.dto.ResourceInfoDto;
import cloudFileStorage.cloudfilestorage.dto.ResourceType;
import cloudFileStorage.cloudfilestorage.exceptions.ResourceAlreadyExistException;
import cloudFileStorage.cloudfilestorage.exceptions.ResourceNotFoundException;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    private final MinioClientService minioClientService;

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

    public ResourceInfoDto getResourceInfo(String path) {

        String separator = "/";

        if (path.endsWith(separator)) {
            path = path.substring(0, path.lastIndexOf(separator));
        }

        int lastIndex = path.lastIndexOf(separator);
        String resourceName = path.substring(lastIndex + 1);
        String resourcePath = path.substring(path.indexOf(separator) + 1, path.lastIndexOf(separator) + 1);

        if (resourcePath.isEmpty()) {
            resourcePath = separator;
        }

        ResourceInfoDto resourceInfoDto = ResourceInfoDto.builder()
                .path(resourcePath)
                .name(resourceName)
                .build();

        try {

            long fileSize = minioClientService.statObject(usersBucket, path).size();

            resourceInfoDto.setSize(fileSize);
            resourceInfoDto.setType(ResourceType.FILE);

        } catch (ErrorResponseException e) {

            if (!minioClientService.isFolderExist(usersBucket, path)) {
                throw new ResourceNotFoundException();
            }

            resourceInfoDto.setName(resourceName + separator);
            resourceInfoDto.setType(ResourceType.DIRECTORY);
        }

        return resourceInfoDto;
    }

    @SneakyThrows
    public void deleteResource(String path) {

        List<DeleteObject> objectsToDelete = new ArrayList<>();

        try {
            minioClientService.getObject(usersBucket, path);
            objectsToDelete.add(new DeleteObject(path));
        } catch (ErrorResponseException e) {

            if (!minioClientService.isFolderExist(usersBucket, path)) {
                throw new ResourceNotFoundException();
            }

            Iterable<Result<Item>> results = minioClientService.listObjects(usersBucket, path, true);

            for (Result<Item> result : results) {
                String itemName = result.get().objectName();
                objectsToDelete.add(new DeleteObject(itemName));
            }
        }

        minioClientService.removeObjects(usersBucket, objectsToDelete);
    }

    @SneakyThrows
    public byte[] downloadResource(String path) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        try {

            return minioClientService.getObject(usersBucket, path).readAllBytes();

        } catch (ErrorResponseException e) {

            if (!minioClientService.isFolderExist(usersBucket, path)) {
                throw new ResourceNotFoundException();
            }

            Iterable<Result<Item>> results = minioClientService.listObjects(usersBucket, path, true);

            for (Result<Item> result : results) {
                Item item = result.get();
                String resourceName = item.objectName();

                String entryName = resourceName.substring(path.length());
                zos.putNextEntry(new ZipEntry(entryName));

                try (InputStream is = minioClientService.getObject(usersBucket, resourceName)) {

                    is.transferTo(zos);
                }

                zos.closeEntry();
            }
            zos.finish();
        }

        return baos.toByteArray();
    }

    @SneakyThrows
    public ResourceInfoDto moveOrRenameResource(String from, String to) {

        try {
            minioClientService.getObject(usersBucket, to);

        } catch (ErrorResponseException e) {

            String movedResourcePath = minioClientService.copyObject(usersBucket, from, to).object();

            minioClientService.removeObjects(usersBucket, List.of(new DeleteObject(from)));

            return getResourceInfo(movedResourcePath);
        }

        throw new ResourceAlreadyExistException();
    }
}
