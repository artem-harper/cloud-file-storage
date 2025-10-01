package cloudFileStorage.cloudfilestorage.controllers;

import cloudFileStorage.cloudfilestorage.service.AuthService;
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

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, String>> signUpUser(){
        authService.signUpUser();
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test(){
        return new ResponseEntity<>(Map.of("test", "work"), HttpStatus.OK);
    }
}
