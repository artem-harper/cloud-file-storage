package cloudFileStorage.cloudfilestorage;

import cloudFileStorage.cloudfilestorage.dto.AuthUserDto;
import cloudFileStorage.cloudfilestorage.dto.SignedUserDto;
import cloudFileStorage.cloudfilestorage.exceptions.UserAlreadyExistException;
import cloudFileStorage.cloudfilestorage.repository.UserRepository;
import cloudFileStorage.cloudfilestorage.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthServiceTest extends BaseIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDb() {
        userRepository.deleteAll();
    }

    @Nested
    class signUp {
        @Test
        public void dbUsersCountShouldUIncrementAfterSignUp() {
            int beforeSignUpDbUsersCount = userRepository.findAll().size();

            AuthUserDto authUserDto = new AuthUserDto("test", "test_password");

            authService.signUpUser(authUserDto);

            int afterSignInDbUsersCount = userRepository.findAll().size();

            assertThat(afterSignInDbUsersCount - beforeSignUpDbUsersCount).isEqualTo(1);
        }

        @Test
        public void userWithNotUniqueUsernameShouldThrowException() {

            AuthUserDto authUserDto1 = new AuthUserDto("user1", "test_password");
            authService.signUpUser(authUserDto1);

            AuthUserDto authUserDto2 = new AuthUserDto("user1", "test_password");

            assertThrows(UserAlreadyExistException.class, () -> authService.signUpUser(authUserDto2));
        }
    }

    @Nested
    class signIn {

        @Test
        public void userWithRightCredentialsShouldSuccessfulSignIn() {
            AuthUserDto authUserDto1 = new AuthUserDto("user1", "test_password");
            authService.signUpUser(authUserDto1);

            AuthUserDto authUserDto2 = new AuthUserDto("user1", "test_password");
            authService.signInUser(authUserDto2);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            assertTrue(authentication.isAuthenticated());
            assertThat(authUserDto2.getUsername()).isEqualTo(userDetails.getUsername());
        }


        @Test
        public void userWithWrongCredentialsShouldThrowException() {
            AuthUserDto authUserDto1 = new AuthUserDto("user1", "test_password");
            authService.signUpUser(authUserDto1);

            AuthUserDto authUserDto2 = new AuthUserDto("wrongUser", "test_password");

            assertThrows(AuthenticationException.class, () -> authService.signInUser(authUserDto2));
        }
    }
}
