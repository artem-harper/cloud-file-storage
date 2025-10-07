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

        InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(path)
                .build());

        return inputStream;

    }

    @SneakyThrows
    public StatObjectResponse statObject(String bucket, String path) throws ErrorResponseException {

        return minioClient.statObject(StatObjectArgs.builder()
                .bucket(bucket)
                .object(path)
                .build());

    }

    @SneakyThrows
    public void removeObjects(String bucket, List<DeleteObject> deleteObjectList) {
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder()
                .bucket(bucket)
                .objects(deleteObjectList)
                .build());

        for (Result<DeleteError> result : results) {
            result.get();
        }
    }

    @SneakyThrows
    public ObjectWriteResponse copyObject(String bucket, String moveFrom, String moveTo) {

        return minioClient.copyObject(CopyObjectArgs.builder()
                .bucket(bucket)
                .object(moveTo)
                .source(
                        CopySource.builder()
                                .bucket(bucket)
                                .object(moveFrom)
                                .build()
                )
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
    public boolean isDirectoryExist(String bucket, String path) {
        return minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucket)
                .prefix(path)
                .maxKeys(1)
                .build()).iterator().hasNext();
    }

}

