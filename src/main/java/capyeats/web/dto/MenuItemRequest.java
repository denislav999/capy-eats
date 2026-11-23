package capyeats.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemRequest {
    @NotBlank
    @Size(max = 30)
    private String name;
    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 4, fraction = 2)
    private BigDecimal price;
    private boolean available;

}
