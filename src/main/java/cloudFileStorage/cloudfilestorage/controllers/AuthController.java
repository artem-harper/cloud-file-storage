package cloudFileStorage.cloudfilestorage.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
@RequestMapping("/api/auth")
public class AuthController {


    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, String>> signUpUser(){
        return null;
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test(){
        return new ResponseEntity<>(Map.of("test", "work"), HttpStatus.OK);
    }
}
