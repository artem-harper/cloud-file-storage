package cloudFileStorage.cloudfilestorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {


        ObjectMapper objectMapper = new ObjectMapper();

        Iterable<Result<Item>> results;
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://127.0.0.1:9000")
                .credentials("minio12345", "minio12345")
                .build();


        String bucket = "user-files";

        List<DeleteObject> list = new ArrayList<>();

        /*Iterable<Result<Item>> results1 = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucket)
                .prefix("user-1-files/folder/")
                .recursive(true)
                .build());

        for (Result<Item> result : results1){
            list.add(new DeleteObject(result.get().objectName()));
        }*/

        list.add(new DeleteObject("user-1-files/folder/374320_20250204172414_1.png"));

        Iterable<Result<DeleteError>> removeResults = minioClient.removeObjects(
                RemoveObjectsArgs.builder()
                        .bucket(bucket)
                        .objects(list)
                        .build()
        );

        for (Result<DeleteError> result : removeResults) {
                DeleteError error = result.get();
        }
    }
}
