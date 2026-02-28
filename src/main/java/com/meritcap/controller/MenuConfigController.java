package com.meritcap.controller;

import com.meritcap.DTOs.responseDTOs.menu.MenuConfigResponseDto;
import com.meritcap.exception.NotFoundException;
import com.meritcap.response.ApiFailureResponse;
import com.meritcap.response.ApiSuccessResponse;
import com.meritcap.service.MenuConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("menu-config")
public class MenuConfigController {

    @Autowired
    private MenuConfigService menuConfigService;

    /**
     * Get menu configuration for the current authenticated user
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMenuConfig(Authentication authentication) {
        try {
            MenuConfigResponseDto responseDto = menuConfigService.getMenuConfigForCurrentUser();
            return ResponseEntity.ok()
                    .body(new ApiSuccessResponse<>(responseDto, "Menu configuration retrieved successfully", 200));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        } catch (Exception e) {
            log.error("Error retrieving menu configuration: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), "Error retrieving menu configuration", 500));
        }
    }

    /**
     * Get menu configuration for a specific user (Admin only)
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getMenuConfigForUser(@PathVariable Long userId, Authentication authentication) {
        try {
            MenuConfigResponseDto responseDto = menuConfigService.getMenuConfigForUser(userId);
            return ResponseEntity.ok()
                    .body(new ApiSuccessResponse<>(responseDto, "Menu configuration retrieved successfully", 200));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        } catch (Exception e) {
            log.error("Error retrieving menu configuration for user {}: ", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), "Error retrieving menu configuration", 500));
        }
    }
}
