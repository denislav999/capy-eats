package capyeats.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class KitchenSettingsRequest {
    @NotBlank(message = "Kitchen name is required")
    @Size(max = 100, message = "Kitchen name must not exceed 100 characters")
    private String name;

    @Size(max = 150, message = "Description must not exceed 150 characters")
    private String description;

    @Min(value = 5, message = "Delivery time must be at least 5 minutes")
    @Max(value = 120, message = "Delivery time must not exceed 120 minutes")
    private Integer deliveryTime;

    @DecimalMin(value = "0.00", message = "Delivery fee cannot be negative")
    @Digits(integer = 4, fraction = 2, message = "Enter a delivery fee up to €9999.99 with at most two decimal places")
    private BigDecimal deliveryFee;

    @NotNull(message = "Choose whether to accept new orders")
    private Boolean open;
}
