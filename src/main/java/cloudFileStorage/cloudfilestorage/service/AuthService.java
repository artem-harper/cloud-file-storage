package cloudFileStorage.cloudfilestorage.service;

import cloudFileStorage.cloudfilestorage.dto.AuthUserDto;

import cloudFileStorage.cloudfilestorage.dto.SignedUserDto;
import cloudFileStorage.cloudfilestorage.entity.User;
import cloudFileStorage.cloudfilestorage.exceptions.UserAlreadyExistException;
import cloudFileStorage.cloudfilestorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ResourceService resourceService;

    public SignedUserDto signUpUser(AuthUserDto authUserDto) {

        if (userRepository.findByUsername(authUserDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistException();
        }

        User signUser = modelMapper.map(authUserDto, User.class);
        signUser.setPassword(passwordEncoder.encode(signUser.getPassword()));

        User signedUser = userRepository.save(signUser);

        saveUserInSecurityContext(authUserDto.getUsername(), authUserDto.getPassword());
        resourceService.createUserFolder(signedUser.getId());

        return modelMapper.map(signedUser, SignedUserDto.class);
    }

    public SignedUserDto signInUser(AuthUserDto authUserDto) {

        Authentication authentication = saveUserInSecurityContext(authUserDto.getUsername(), authUserDto.getPassword());

        return modelMapper.map(authentication.getPrincipal(), SignedUserDto.class);
    }

    private Authentication saveUserInSecurityContext(String username, String password) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        return authenticate;

    }
}
