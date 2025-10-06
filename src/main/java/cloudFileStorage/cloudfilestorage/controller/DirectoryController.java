package cloudFileStorage.cloudfilestorage.controller;

import cloudFileStorage.cloudfilestorage.dto.ResourceInfoDto;
import cloudFileStorage.cloudfilestorage.security.UserDetailsImpl;
import cloudFileStorage.cloudfilestorage.service.DirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directory")
public class DirectoryController {

    private final DirectoryService directoryService;

    @GetMapping()
    public ResponseEntity<List<ResourceInfoDto>> getDirectoryInfo(@RequestParam("path") String path,
                                                                  @AuthenticationPrincipal UserDetailsImpl userDetails){


        String userFolder = "user-%s-files/".formatted(userDetails.getId());


        List<ResourceInfoDto> resourceInfoDtoList = directoryService.getDirectoryInfo(userFolder+path);


        return new ResponseEntity<>(resourceInfoDtoList, HttpStatus.OK);

    }

}
