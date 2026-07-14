package capyeats.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Empty username!")
    @Size(min = 6, max = 26, message = "Username must be between 6 and 26 symbols.")
    private String username;

    @NotBlank(message = "Empty password!")
    @Size(min = 6, max = 18, message = "Password must be between 6 and 18 symbols.")
    private String password;
}