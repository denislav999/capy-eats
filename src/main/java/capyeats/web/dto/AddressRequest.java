package capyeats.web.dto;

import capyeats.address.model.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    @NotBlank
    @Size(min = 3,max = 20, message = "Label must be between 3 and 20 characters")
    private String label;
    @NotNull(message = "Select an address type")
    private AddressType addressType;
    @NotBlank
    @Size(max=30)
    private String city;
    @NotBlank
    @Pattern(regexp = "[0-9]{4}", message = "Postal code must contain exactly 4 digits")
    private String postalCode;
    @NotBlank
    @Size(max = 30)
    private String street;
    @NotBlank
    @Size(max = 10)
    private String streetNumber;
    @Size(max = 20)
    private String apartment;

    AddressRequest(String label, AddressType addressType, String city, String postalCode, String street, String streetNumber){
        this.label = label;
        this.addressType = addressType;
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.streetNumber = streetNumber;
    }

}
