package cloudFileStorage.cloudfilestorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {


        ObjectMapper objectMapper = new ObjectMapper();

        Iterable<Result<Item>> results;
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://127.0.0.1:9000")
                .credentials("minio12345", "minio12345")
                .build();


        boolean a = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket("user-files")
                        .prefix("user-1-files/test-folder")
                        .maxKeys(1)
                .build()).iterator().hasNext();

        System.out.println();
    }
}
