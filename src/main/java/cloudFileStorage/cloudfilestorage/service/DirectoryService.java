package cloudFileStorage.cloudfilestorage.service;

import cloudFileStorage.cloudfilestorage.dto.ResourceInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectoryService {

    private final ResourceService resourceService;

    public List<ResourceInfoDto> getDirectoryInfo(String s) {

        List<ResourceInfoDto> resourceInfoDtoList = new ArrayList<>();

        return resourceInfoDtoList;
    }
}
