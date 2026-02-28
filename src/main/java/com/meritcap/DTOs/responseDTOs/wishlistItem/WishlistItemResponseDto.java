package com.meritcap.DTOs.responseDTOs.wishlistItem;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class WishlistItemResponseDto {
    Long wishlistItemId;
    Long studentId;
    Long collegeCourseId;
}
