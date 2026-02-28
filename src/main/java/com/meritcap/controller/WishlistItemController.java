package com.meritcap.controller;
import com.meritcap.DTOs.responseDTOs.wishlistItem.WishlistItemResponseDto;
import com.meritcap.exception.NotFoundException;
import com.meritcap.response.ApiFailureResponse;
import com.meritcap.response.ApiSuccessResponse;
import com.meritcap.service.WishlistItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/wishlist-item")
public class WishlistItemController {

    private final WishlistItemService wishlistItemService;

    public WishlistItemController(WishlistItemService wishlistItemService) {
        this.wishlistItemService = wishlistItemService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addWishlistItem(@RequestParam Long studentId, @RequestParam Long collegeCourseId) {
        try{
            WishlistItemResponseDto wishlistItemResponseDto = wishlistItemService.addWishlistItem(studentId, collegeCourseId);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiSuccessResponse<>(wishlistItemResponseDto, "College course added to cart successfully", 201));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @GetMapping("/getWishlistItems")
    public ResponseEntity<?> getWishlistItems(@RequestParam Long studentId) {
        try{
            List<WishlistItemResponseDto> wishlistItemResponseDtos = wishlistItemService.getWishlistItems(studentId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(wishlistItemResponseDtos, "College course fetched successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @DeleteMapping("/remove/{wishlistItemId}")
    public ResponseEntity<?> removeWishlistItem(@PathVariable Long wishlistItemId) {
        try{
            WishlistItemResponseDto wishlistItemResponseDto = wishlistItemService.removeWishlistItem(wishlistItemId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(wishlistItemResponseDto, "College course removed from cart successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }
}
