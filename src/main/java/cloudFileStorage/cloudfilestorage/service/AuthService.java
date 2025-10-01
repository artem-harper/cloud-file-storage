package cloudFileStorage.cloudfilestorage.service;

import cloudFileStorage.cloudfilestorage.dto.AuthUserDto;
import cloudFileStorage.cloudfilestorage.dto.SignedUpUserDto;
import cloudFileStorage.cloudfilestorage.entity.User;
import cloudFileStorage.cloudfilestorage.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public AuthService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public SignedUpUserDto signUpUser(AuthUserDto authUserDto) {
        User user = modelMapper.map(authUserDto, User.class);
        return modelMapper.map(userRepository.save(user), SignedUpUserDto.class);
    }
}
