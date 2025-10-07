package cloudFileStorage.cloudfilestorage.controller;

import cloudFileStorage.cloudfilestorage.dto.ResourceInfoDto;
import cloudFileStorage.cloudfilestorage.security.UserDetailsImpl;
import cloudFileStorage.cloudfilestorage.service.ResourceService;
import cloudFileStorage.cloudfilestorage.util.PathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    private final ResourceService resourceService;
    private final PathUtil pathUtil;

    @GetMapping()
    public ResponseEntity<ResourceInfoDto> getResourceInfo(@RequestParam("path") String path,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userFolder = pathUtil.getUserDirectoryName(userDetails.getId());

        ResourceInfoDto resourceInfo = resourceService.getResourceInfo(userFolder + path);

        return new ResponseEntity<>(resourceInfo, HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadResource(@RequestParam("path") String path,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        String formattedPath = pathUtil.removeFirstSlash(path);

        String userDirectory = pathUtil.getUserDirectoryName(userDetails.getId());

        byte[] downloadedResource = resourceService.downloadResource(userDirectory + formattedPath);

        return new ResponseEntity<>(downloadedResource, HttpStatus.OK);
    }

    @GetMapping("/move")
    public ResponseEntity<ResourceInfoDto> moveOrRenameResource(@RequestParam("from") String from,
                                                                @RequestParam("to") String to,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userDirectory = pathUtil.getUserDirectoryName(userDetails.getId());

        ResourceInfoDto resourceInfoDto = resourceService.moveOrRenameResource(userDirectory + from, userDirectory + to);
        return new ResponseEntity<>(resourceInfoDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResourceInfoDto>> searchResource(@RequestParam("query") String query,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails){


        String userDirectory = pathUtil.getUserDirectoryName(userDetails.getId());
        List<ResourceInfoDto> resourceInfoDtoList = resourceService.searchResource(userDirectory, query);


        return new ResponseEntity<>(resourceInfoDtoList, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteResource(@RequestParam("path") String path,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String formattedPath = pathUtil.removeFirstSlash(path);

        String userDirectory = pathUtil.getUserDirectoryName(userDetails.getId());

        resourceService.deleteResource(userDirectory + formattedPath);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
