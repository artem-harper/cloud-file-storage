package cloudFileStorage.cloudfilestorage;

import cloudFileStorage.cloudfilestorage.dto.AuthUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends BaseIntegrationTest{

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    public void securityShouldThrow401CodeForUnauthorizedUser() {

        mockMvc.perform(get("http://localhost:8080/api/user/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @SneakyThrows
    public void securityShouldThrow200CodeForSignUpUser(){

        AuthUserDto authUserDto = new AuthUserDto("user1", "12345");

        mockMvc.perform(post("http://localhost:8080/api/auth/sign-up")
                        .content(objectMapper.writeValueAsString(authUserDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }
}















