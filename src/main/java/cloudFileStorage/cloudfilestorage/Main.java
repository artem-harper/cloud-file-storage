package cloudFileStorage.cloudfilestorage;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {


        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint("http://127.0.0.1:9000")
                        .credentials("minioadmin", "minioadmin")
                        .build();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket("user-files")
                        .object("user-1-files/")
                        .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                        .build()
        );
    }
}
