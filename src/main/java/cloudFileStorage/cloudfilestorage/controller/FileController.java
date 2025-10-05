package cloudFileStorage.cloudfilestorage.controller;

import cloudFileStorage.cloudfilestorage.dto.FileInfoDto;
import cloudFileStorage.cloudfilestorage.entity.User;
import cloudFileStorage.cloudfilestorage.security.UserDetailsImpl;
import cloudFileStorage.cloudfilestorage.service.UserFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FileController {

    private final UserFileService userFileService;
    private final UserController userController;

    @GetMapping("/resource")
    public ResponseEntity<FileInfoDto> getResourceInfo(@RequestParam("path") String path,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String userFolder = "user-%s-files/".formatted(userDetails.getId());

        FileInfoDto resourceInfo = userFileService.getResourceInfo(path, userFolder);

        return new ResponseEntity<>(resourceInfo, HttpStatus.OK);
    }

    @GetMapping("/directory")
    public ResponseEntity<FileInfoDto> getFolderFilesInfo(@RequestParam("path") String path) {

        return null;
    }
}
