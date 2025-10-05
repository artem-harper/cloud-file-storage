package cloudFileStorage.cloudfilestorage.controller;

import cloudFileStorage.cloudfilestorage.dto.ResourceInfoDto;
import cloudFileStorage.cloudfilestorage.security.UserDetailsImpl;
import cloudFileStorage.cloudfilestorage.service.UserResourceService;
import cloudFileStorage.cloudfilestorage.util.ErrorResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ResourceController {

    private final UserResourceService userResourceService;
    private final UserController userController;

    @GetMapping("/resource")
    public ResponseEntity<ResourceInfoDto> getResourceInfo(@RequestParam("path") String path,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userFolder = "user-%s-files/".formatted(userDetails.getId());

        ResourceInfoDto resourceInfo = userResourceService.getResourceInfo(userFolder, path);

        return new ResponseEntity<>(resourceInfo, HttpStatus.OK);
    }

    @DeleteMapping("/resource")
    public ResponseEntity<Void> deleteResource(@RequestParam("path") String path,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails){

        String userFolder = "user-%s-files/".formatted(userDetails.getId());

        userResourceService.deleteResource(userFolder, path);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/directory")
    public ResponseEntity<ResourceInfoDto> getFolderFilesInfo(@RequestParam("path") String path) {

        return null;
    }
}
