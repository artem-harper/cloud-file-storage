package cloudFileStorage.cloudfilestorage.service;

import cloudFileStorage.cloudfilestorage.config.MinioConfig;
import io.minio.GetObjectArgs;
import lombok.RequiredArgsConstructor;
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

    public void getResourceInfo(String path) {

    }
}
