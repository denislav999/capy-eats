package capyeats.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @Size(max = 32, message = "Phone number must not exceed 32 characters")
    @Pattern(regexp = "(?:|(?=.*[0-9])\\+?[0-9(). -]+)", message = "Enter a valid phone number")
    private String phoneNumber;

    @NotBlank(message = "Username is required")
    @Size(min = 6, max = 26, message = "Username must be between 6 and 26 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Size(max = 40, message = "Email must not exceed 40 characters")
    private String email;

    @Size(min = 6, max = 18, message = "Password must be between 6 and 18 characters")
    private String password;
}
