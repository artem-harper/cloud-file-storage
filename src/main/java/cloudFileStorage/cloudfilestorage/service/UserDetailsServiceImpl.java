package cloudFileStorage.cloudfilestorage.service;

import cloudFileStorage.cloudfilestorage.entity.User;
import cloudFileStorage.cloudfilestorage.repository.UserRepository;
import cloudFileStorage.cloudfilestorage.security.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> maybeUser = userRepository.findByUsername(username);

        if (maybeUser.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        return new UserDetailsImpl(maybeUser.get());
    }
}
