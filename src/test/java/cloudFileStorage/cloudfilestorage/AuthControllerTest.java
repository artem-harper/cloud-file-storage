package cloudFileStorage.cloudfilestorage;

import cloudFileStorage.cloudfilestorage.dto.AuthUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthControllerTest extends BaseIntegrationTest{

    @Autowired
    protected MockMvc mockMvc;


    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    public void test() {

        AuthUserDto authUserDto = new AuthUserDto("test", "test_pass");

        String requestBody = objectMapper.writeValueAsString(authUserDto);

        mockMvc.perform(post("http://localhost:8080/api/auth/sign-up")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().is2xxSuccessful());

    }
}















