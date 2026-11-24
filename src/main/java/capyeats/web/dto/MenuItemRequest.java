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
    @NotBlank(message = "Name required.")
    @Size(max = 30, message = "Max 30 characters.")
    private String name;
    @NotNull(message = "Price required.")
    @DecimalMin(value = "0.01", message = "Min €0.01.")
    @Digits(integer = 4, fraction = 2, message = "Max €9,999.99, 2 decimals.")
    private BigDecimal price;
    private boolean available;

}
