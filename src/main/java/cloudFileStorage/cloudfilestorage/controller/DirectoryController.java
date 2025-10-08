package cloudFileStorage.cloudfilestorage.controller;

import cloudFileStorage.cloudfilestorage.dto.ResourceInfoDto;
import cloudFileStorage.cloudfilestorage.security.UserDetailsImpl;
import cloudFileStorage.cloudfilestorage.service.DirectoryService;
import cloudFileStorage.cloudfilestorage.util.PathUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Directory controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directory")
public class DirectoryController {

    private final DirectoryService directoryService;
    private final PathUtil pathUtil;

    @Operation(
            summary = "Get information about files in directory"
    )
    @GetMapping()
    public ResponseEntity<List<ResourceInfoDto>> getDirectoryInfo(@RequestParam("path") String path,
                                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userFolder = pathUtil.getUserDirectoryName(userDetails.getId());
        String s = pathUtil.removeFirstSlash(path);
        List<ResourceInfoDto> resourceInfoDtoList = directoryService.getDirectoryInfo(userFolder+s);

        return new ResponseEntity<>(resourceInfoDtoList, HttpStatus.OK);
    }

    @Operation(
            summary = "Create empty directory"
    )
    @PostMapping()
    public ResponseEntity<ResourceInfoDto> createEmptyDirectory(@RequestParam("path") String path,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails){

        String userFolder = pathUtil.getUserDirectoryName(userDetails.getId());

        ResourceInfoDto emptyDirectory = directoryService.createEmptyDirectory(userFolder+path);

        return new ResponseEntity<>(emptyDirectory, HttpStatus.CREATED);
    }
}
