package com.meritcap.service;

import com.meritcap.DTOs.responseDTOs.menu.MenuConfigResponseDto;

public interface MenuConfigService {

    /**
     * Get menu configuration for a specific user based on their permissions
     * 
     * @param userId User ID
     * @return Menu configuration with visibility and features
     */
    MenuConfigResponseDto getMenuConfigForUser(Long userId);

    /**
     * Get menu configuration for the currently authenticated user
     * 
     * @return Menu configuration with visibility and features
     */
    MenuConfigResponseDto getMenuConfigForCurrentUser();
}
