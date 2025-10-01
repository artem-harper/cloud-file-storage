package cloudFileStorage.cloudfilestorage.controller;

import cloudFileStorage.cloudfilestorage.dto.AuthUserDto;
import cloudFileStorage.cloudfilestorage.dto.SignedUpUserDto;
import cloudFileStorage.cloudfilestorage.service.AuthService;
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
    public ResponseEntity<SignedUpUserDto> signUpUser(@RequestBody AuthUserDto authUserDto){
        SignedUpUserDto signUpUser = authService.signUpUser(authUserDto);

        return ResponseEntity.ok()
                .body(signUpUser);

    }


    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test(){
        return new ResponseEntity<>(Map.of("test", "work"), HttpStatus.OK);
    }
}
