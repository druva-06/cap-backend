package com.meritcap.DTOs.requestDTOs.address;

import com.meritcap.enums.AddressType;
import com.meritcap.enums.EntityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AddressRequestDto {

    @NotBlank(message = "Address Line 1 cannot be blank")
    @Size(max = 255, message = "Address Line 1 must not exceed 255 characters")
    String addressLine1;

    @NotBlank(message = "Address Line 1 cannot be blank")
    @Size(max = 255, message = "Address Line 2 must not exceed 255 characters")
    String addressLine2;

    @NotBlank(message = "City/Village cannot be blank")
    @Size(max = 100, message = "City/Village must not exceed 100 characters")
    String cityVillage;

    @NotBlank(message = "PIN Code cannot be blank")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid PIN Code")
    String pinCode;

    @Size(max = 255, message = "Landmark must not exceed 255 characters")
    String landmark;

    @NotBlank(message = "State cannot be blank")
    @Size(max = 100, message = "State must not exceed 100 characters")
    String state;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 100, message = "Country must not exceed 100 characters")
    String country;

    @NotNull(message = "Address type is required")
    AddressType addressType;

    @NotNull(message = "Entity type is required")
    EntityType entityType;
}
