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
    @Size(min = 4, max = 30, message = "Username length should between 4 and 30 symbols")
    private String username;

    @JsonProperty("password")
    @NotNull(message = "Password can't be empty")
    @Size(min = 4, max = 30, message = "Password length should between 4 and 30 symbols")
    private String password;


}
