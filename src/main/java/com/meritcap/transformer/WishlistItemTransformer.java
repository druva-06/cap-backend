package com.meritcap.transformer;

import com.meritcap.DTOs.responseDTOs.wishlistItem.WishlistItemResponseDto;
import com.meritcap.model.WishlistItem;

import java.util.ArrayList;
import java.util.List;

public class WishlistItemTransformer {
    public static WishlistItemResponseDto toResDTO(WishlistItem wishlistItem, Long studentId) {
        return WishlistItemResponseDto.builder()
                .wishlistItemId(wishlistItem.getId())
                .collegeCourseId(wishlistItem.getCollegeCourse().getId())
                .studentId(studentId)
                .build();
    }

    public static List<WishlistItemResponseDto> toResDTO(List<WishlistItem> wishlistItems, Long studentId) {
        List<WishlistItemResponseDto> wishlistItemResponseDtos = new ArrayList<>();

        for (WishlistItem wishlistItem : wishlistItems) {
            wishlistItemResponseDtos.add(toResDTO(wishlistItem, studentId));
        }
        return wishlistItemResponseDtos;
    }
}
