package cloudFileStorage.cloudfilestorage.controller;

import cloudFileStorage.cloudfilestorage.dto.ResourceInfoDto;
import cloudFileStorage.cloudfilestorage.security.UserDetailsImpl;
import cloudFileStorage.cloudfilestorage.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    private final ResourceService resourceService;
    private final UserController userController;

    @GetMapping()
    public ResponseEntity<ResourceInfoDto> getResourceInfo(@RequestParam("path") String path,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userFolder = "user-%s-files/".formatted(userDetails.getId());

        ResourceInfoDto resourceInfo = resourceService.getResourceInfo(userFolder+path);

        return new ResponseEntity<>(resourceInfo, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteResource(@RequestParam("path") String path,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userFolder = "user-%s-files/".formatted(userDetails.getId());

        resourceService.deleteResource(userFolder+ path);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/download")
    public ResponseEntity<InputStream> downloadResource(@RequestParam("path")  String path,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userFolder = "user-%s-files/".formatted(userDetails.getId());

        InputStream inputStream = resourceService.downloadResource(userFolder, path);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(inputStream);
    }



}
