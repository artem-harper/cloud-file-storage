package cloudFileStorage.cloudfilestorage;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {


        Iterable<Result<Item>> results;
        try (MinioClient minioClient = MinioClient.builder()
                .endpoint("http://127.0.0.1:9000")
                .credentials("minioadmin", "minioadmin")
                .build()) {

            results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket("user-files")
                            .prefix("user-1-files/")
                            .build());


            for (Result<Item> result : results) {
                Item item = result.get();
                String s = item.objectName();
                System.out.println();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
