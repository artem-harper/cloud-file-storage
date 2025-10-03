package cloudFileStorage.cloudfilestorage.controller;

import cloudFileStorage.cloudfilestorage.dto.AuthUserDto;
import cloudFileStorage.cloudfilestorage.dto.SignedUserDto;
import cloudFileStorage.cloudfilestorage.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignedUserDto> signUpUser(@RequestBody @Valid AuthUserDto authUserDto) {
        SignedUserDto signedUpUser = authService.signUpUser(authUserDto);

        return new ResponseEntity<>(signedUpUser, HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignedUserDto> signInUser(@RequestBody @Valid AuthUserDto authUserDto) {
        SignedUserDto signedInUserDto = authService.signInUser(authUserDto);
        return new ResponseEntity<>(signedInUserDto, HttpStatus.OK);
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
