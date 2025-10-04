package cloudFileStorage.cloudfilestorage.controller;

import cloudFileStorage.cloudfilestorage.dto.FileDto;
import cloudFileStorage.cloudfilestorage.dto.SignedUserDto;
import cloudFileStorage.cloudfilestorage.service.UserFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<FileDto> getFileInfo(@RequestParam("path") String path) {
        userFileService.getResourceInfo(path);
        return null;
    }

    @GetMapping("/directory")
    public ResponseEntity<FileDto> getDirectoryInfo(@RequestParam("path") String path) {

        UserDetails principal = (UserDetails)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();


        System.out.println();

        return null;
    }
}
