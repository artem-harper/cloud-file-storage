package cloudFileStorage.cloudfilestorage.controller;

import cloudFileStorage.cloudfilestorage.dto.AuthUserDto;
import cloudFileStorage.cloudfilestorage.dto.SignedUserDto;
import cloudFileStorage.cloudfilestorage.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignedUserDto> signUpUser(@RequestBody @Valid AuthUserDto authUserDto) {
        SignedUserDto signUpUser = authService.signUpUser(authUserDto);

        return new ResponseEntity<>(signUpUser, HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignedUserDto> signInUser(@RequestBody @Valid AuthUserDto authUserDto) {
        return new ResponseEntity<>(authService.signInUser(authUserDto), HttpStatus.OK);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<SignedUserDto> logoutUser() {
        authService.logoutUser();
        return null;
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        return new ResponseEntity<>(Map.of("test", "work"), HttpStatus.OK);
    }
}
