package com.meritcap.DTOs.responseDTOs.address;

import com.meritcap.enums.AddressType;
import com.meritcap.enums.EntityType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AddressResponseDto {
    Long addressId;
    Long entityId;
    String addressLine1;
    String addressLine2;
    String cityVillage;
    String pinCode;
    String landmark;
    String state;
    String country;
    AddressType addressType;
    EntityType entityType;
}
