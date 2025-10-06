package cloudFileStorage.cloudfilestorage.controller;

import cloudFileStorage.cloudfilestorage.dto.AuthUserDto;
import cloudFileStorage.cloudfilestorage.dto.SignedUserDto;
import cloudFileStorage.cloudfilestorage.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return new ResponseEntity<>(new SignedUserDto(), HttpStatus.NO_CONTENT);
    }
}
