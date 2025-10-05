package cloudFileStorage.cloudfilestorage.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    @SneakyThrows
    public MinioClient minioClient() {

        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket("user-files")
                .build());

        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket("user-files")
                    .build());
        }

        return minioClient;
    }

}
