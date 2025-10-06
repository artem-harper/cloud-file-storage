package cloudFileStorage.cloudfilestorage.service;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.*;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioClientService {

    private final MinioClient minioClient;


    @SneakyThrows
    public InputStream getObject(String bucket, String path) throws ErrorResponseException {

        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(path)
                .build());

    }

    @SneakyThrows
    public StatObjectResponse statObject(String bucket, String path) throws ErrorResponseException {

        return minioClient.statObject(StatObjectArgs.builder()
                .bucket(bucket)
                .object(path)
                .build());

    }

    public Iterable<Result<DeleteError>> removeObjects(String bucket, List<DeleteObject> deleteObjectList) {
        return minioClient.removeObjects(RemoveObjectsArgs.builder()
                .bucket(bucket)
                .objects(deleteObjectList)
                .build());
    }

    @SneakyThrows
    public Iterable<Result<Item>> listObjects(String bucket, String path, boolean recursive) {

        return minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucket)
                .prefix(path)
                .recursive(recursive)
                .build());

    }

    @SneakyThrows
    public boolean isFolderExist(String bucket, String path) {
        return minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucket)
                .prefix(path)
                .maxKeys(1)
                .build()).iterator().hasNext();
    }

}

