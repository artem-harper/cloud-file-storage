package cloudFileStorage.cloudfilestorage.controller;

import cloudFileStorage.cloudfilestorage.service.UserFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FileController {

    private final UserFileService userFileService;

    @GetMapping("/resource")
    public ResponseEntity<Void> getFile(@RequestParam("path") String path) {
        userFileService.getResourceInfo(path);
        return null;
    }
}
