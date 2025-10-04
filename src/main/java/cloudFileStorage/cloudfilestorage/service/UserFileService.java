package cloudFileStorage.cloudfilestorage.service;

import cloudFileStorage.cloudfilestorage.config.MinioConfig;
import io.minio.GetObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFileService {


    private final MinioConfig minioConfig;
    private final ModelMapper modelMapper;

    @Value("${minio.root-bucket}")
    private String usersBucket;

    @SneakyThrows
    public void getResourceInfo(String path) {

        minioConfig.minioClient().getObject(
                GetObjectArgs.builder()
                        .bucket(usersBucket)
                        .object(path)
                        .build());

    }

    public void getFolderInfo(String path) {
    }
}
