package cloudFileStorage.cloudfilestorage;

import cloudFileStorage.cloudfilestorage.dto.AuthUserDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.support.ParameterDeclarations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ValidatorTest {

    @Autowired
    private Validator validator;

    @ParameterizedTest
    @ArgumentsSource(AuthUserDtoArgumentsProvider.class)
    public void userWithNonValidCredentialsShouldThrowException(AuthUserDto authUserDto) {

        Set<ConstraintViolation<AuthUserDto>> violations = validator.validate(authUserDto);

        assertThat(violations.isEmpty()).isFalse();
    }

    static class AuthUserDtoArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of(new AuthUserDto("i", "test_password")),
                    Arguments.of(new AuthUserDto("varyverylongnamefortestingthiscase", "test")),
                    Arguments.of(new AuthUserDto("test", "varyverylongpasswordfortestingthiscase")),
                    Arguments.of(new AuthUserDto("null", "test"))
            );
        }
    }
}
