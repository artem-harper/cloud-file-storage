package cloudFileStorage.cloudfilestorage.controller;

import cloudFileStorage.cloudfilestorage.dto.ResourceInfoDto;
import cloudFileStorage.cloudfilestorage.security.UserDetailsImpl;
import cloudFileStorage.cloudfilestorage.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping()
    public ResponseEntity<ResourceInfoDto> getResourceInfo(@RequestParam("path") String path,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userFolder = "user-%s-files/".formatted(userDetails.getId());

        ResourceInfoDto resourceInfo = resourceService.getResourceInfo(userFolder + path);

        return new ResponseEntity<>(resourceInfo, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteResource(@RequestParam("path") String path,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (path.startsWith("/")) {
            path = path.substring(path.indexOf("/") + 1);
        }

        String userFolder = "user-%s-files/".formatted(userDetails.getId());

        resourceService.deleteResource(userFolder + path);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadResource(@RequestParam("path") String path,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        if (path.startsWith("/")) {
            path = path.substring(path.indexOf("/") + 1);
        }

        String userFolder = "user-%s-files/".formatted(userDetails.getId());

        byte[] downloadedResource = resourceService.downloadResource(userFolder + path);

        return new ResponseEntity<>(downloadedResource, HttpStatus.OK);
    }

}
