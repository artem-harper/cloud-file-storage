package cloudFileStorage.cloudfilestorage;

import cloudFileStorage.cloudfilestorage.dto.AuthUserDto;
import cloudFileStorage.cloudfilestorage.repository.UserRepository;
import cloudFileStorage.cloudfilestorage.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import static org.assertj.core.api.Assertions.assertThat;

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
        public void userWithNotUniqueUsernameShouldThrowException(){

            AuthUserDto authUserDto = new AuthUserDto("user1", "test_password");

        }

    }

    @Nested
    class signIn {



    }
}
