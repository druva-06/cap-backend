package com.meritcap.DTOs.requestDTOs.college;

import com.meritcap.DTOs.requestDTOs.address.AddressRequestDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CollegeAndAddressRequestDto {
    CollegeRequestDto college;
    AddressRequestDto address;
}
