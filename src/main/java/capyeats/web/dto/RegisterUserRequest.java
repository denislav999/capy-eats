package capyeats.web.dto;

import jakarta.validation.constraints.Email;
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
public class RegisterUserRequest {
    @NotBlank(message = "Empty username!")
    @Size(min = 6, max = 26, message = "Username must be between 6 and 26 symbols.")
    private String username;
    @NotBlank(message = "Empty email!")
    @Email(message = "Email must be valid!")
    @Size(max = 40, message = "Maximum 40 characters!")
    private String email;
    @NotBlank(message = "Password is required!")
    @Size(min = 6, max = 18, message = "Password must be between 6 and 18 characters!")
    private String password;
}
