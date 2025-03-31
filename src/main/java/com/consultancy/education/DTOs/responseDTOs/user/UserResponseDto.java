package com.consultancy.education.DTOs.responseDTOs.user;

import com.consultancy.education.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserResponseDto {
    Long userId;
    String name;
    String email;
    String phoneNumber;
    String designation;
    String profilePicture;
    Role role;
}
