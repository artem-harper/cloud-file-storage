package cloudFileStorage.cloudfilestorage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AuthUserDto {

    @JsonProperty("username")
    @NotNull(message = "Username can't be empty")
    @Size(min = 5, max = 20, message = "Username length should between 4 and 20 symbols")
    private String username;

    @JsonProperty("password")
    @NotNull(message = "Password can't be empty")
    @Size(min = 5, max = 20, message = "Password length should between 4 and 20 symbols")
    private String password;


}
