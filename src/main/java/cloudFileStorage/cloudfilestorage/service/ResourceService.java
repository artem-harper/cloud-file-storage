package cloudFileStorage.cloudfilestorage.service;

import cloudFileStorage.cloudfilestorage.dto.ResourceInfoDto;
import cloudFileStorage.cloudfilestorage.dto.ResourceType;
import cloudFileStorage.cloudfilestorage.exceptions.ResourceAlreadyExistException;
import cloudFileStorage.cloudfilestorage.exceptions.ResourceNotFoundException;
import cloudFileStorage.cloudfilestorage.util.PathUtil;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final MinioClientService minioClientService;
    private final PathUtil pathUtil;

    @Value("${minio.root-bucket}")
    private String usersBucket;

    @SneakyThrows
    public ResourceInfoDto getResourceInfo(String path) {

        String formatPath = pathUtil.removeLastSlash(path);

        String resourceName = pathUtil.getResourceNameFromPath(formatPath);
        String resourcePath = pathUtil.getResourcePath(formatPath);

        ResourceInfoDto resourceInfoDto = ResourceInfoDto.builder()
                .path(resourcePath)
                .name(resourceName)
                .build();


        if (minioClientService.isFileExist(usersBucket, formatPath)) {
            long fileSize = minioClientService.statObject(usersBucket, formatPath).size();

            resourceInfoDto.setSize(fileSize);
            resourceInfoDto.setType(ResourceType.FILE);
        } else if (minioClientService.isDirectoryExist(usersBucket, formatPath)) {

            resourceInfoDto.setName(resourceName + pathUtil.getSEPARATOR());
            resourceInfoDto.setType(ResourceType.DIRECTORY);
        } else {
            throw new ResourceNotFoundException();
        }

        return resourceInfoDto;
    }


    @SneakyThrows
    public void deleteResource(String path) {

        List<DeleteObject> objectsToDelete = new ArrayList<>();


        if (minioClientService.isFileExist(usersBucket, path)) {

            objectsToDelete.add(new DeleteObject(path));

        } else if (minioClientService.isDirectoryExist(usersBucket, path)) {
            Iterable<Result<Item>> results = minioClientService.listObjects(usersBucket, path, true);

            for (Result<Item> result : results) {
                String itemName = result.get().objectName();
                objectsToDelete.add(new DeleteObject(itemName));
            }
        } else {
            throw new ResourceNotFoundException();
        }


        minioClientService.removeObjects(usersBucket, objectsToDelete);
    }

    @SneakyThrows
    public byte[] downloadResource(String path) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);


        if (minioClientService.isFileExist(usersBucket, path)) {

            return minioClientService.getObject(usersBucket, path).readAllBytes();

        } else if (minioClientService.isDirectoryExist(usersBucket, path)) {
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

            return baos.toByteArray();
        } else {
            throw new ResourceNotFoundException();
        }

    }

    @SneakyThrows
    public ResourceInfoDto moveOrRenameResource(String from, String to) {

        if (minioClientService.isFileExist(usersBucket, to)) {
            throw new ResourceAlreadyExistException();
        }

        String movedResourcePath = minioClientService.copyObject(usersBucket, from, to).object();
        minioClientService.removeObjects(usersBucket, List.of(new DeleteObject(from)));
        return getResourceInfo(movedResourcePath);

    }

    @SneakyThrows
    public List<ResourceInfoDto> searchResource(String userDirectory, String query) {

        List<ResourceInfoDto> foundResources = new ArrayList<>();

        Iterable<Result<Item>> results = minioClientService.listObjects(usersBucket, userDirectory, true);

        for (Result<Item> itemResult : results) {
            String path = itemResult.get().objectName();

            if (pathUtil.isRootDirectory(path)) {
                continue;
            }

            String resourceName = pathUtil.getResourceNameFromPath(path).toLowerCase();

            if (resourceName.contains(query.toLowerCase())) {
                foundResources.add(getResourceInfo(path));
            }
        }

        return foundResources;
    }

    public List<ResourceInfoDto> uploadResource(String path, MultipartFile[] multipartFileArr) {



        List<ResourceInfoDto> resourceInfoDtoList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFileArr) {

            if (minioClientService.isFileExist(usersBucket, path+multipartFile.getResource().getFilename()) || minioClientService.isFileExist(usersBucket, path+multipartFile.getResource().getFilename())) {
                throw new ResourceAlreadyExistException();
            }

            String object = minioClientService.uploadResource(usersBucket, path, multipartFile).object();
            resourceInfoDtoList.add(getResourceInfo(object));
        }

        return resourceInfoDtoList;
    }
}
