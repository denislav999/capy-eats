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
public class KitchenOwnerRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 6, max = 26, message = "Username must be between 6 and 26 characters")
    private String username;

    @NotBlank(message = "Kitchen name is required")
    @Size(max = 100, message = "Kitchen name must not exceed 100 characters")
    private String kitchenName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    @Size(max = 40, message = "Email must not exceed 40 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 18, message = "Password must be between 6 and 18 characters")
    private String password;
}