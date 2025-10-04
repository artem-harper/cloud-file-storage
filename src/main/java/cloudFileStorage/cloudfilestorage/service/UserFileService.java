package cloudFileStorage.cloudfilestorage.service;

import cloudFileStorage.cloudfilestorage.config.MinioConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFileService {


    private final MinioConfig minioConfig;
    private final ModelMapper modelMapper;



}
