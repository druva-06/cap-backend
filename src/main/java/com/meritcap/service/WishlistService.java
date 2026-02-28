package com.meritcap.service;


import com.meritcap.DTOs.requestDTOs.wishlistItem.AddWishlistItemRequest;
import com.meritcap.DTOs.responseDTOs.wishlistItem.WishlistItemResponse;

import java.util.List;

public interface WishlistService {
    WishlistItemResponse addWishlistItem(Long studentId, AddWishlistItemRequest request);

    List<WishlistItemResponse> getWishlistItems(Long studentId);

    void removeWishlistItem(Long studentId, Long wishlistItemId);

    int getWishlistItemCount(Long studentId);
}
