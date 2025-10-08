package cloudFileStorage.cloudfilestorage.service;

import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
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
    public ObjectWriteResponse uploadResource(String bucket, String path, MultipartFile multipart) {

        ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(path + multipart.getResource().getFilename())
                .stream(multipart.getInputStream(), multipart.getSize(), 10485760)
                .build());

        return objectWriteResponse;

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
            DeleteError deleteError = result.get();
            log.error("FAILED TO DELETE: {}", deleteError.message());
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

    @SneakyThrows
    public boolean isFileExist(String bucket, String path) {
        try {
            minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(path)
                    .build());

            return true;

        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            throw new MinioException();
        }
    }
}

