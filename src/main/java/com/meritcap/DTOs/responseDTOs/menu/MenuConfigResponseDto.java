package com.meritcap.DTOs.responseDTOs.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuConfigResponseDto {
    private Long userId;
    private String username;
    private String roleName;
    private List<MenuItemDto> menuItems;
    private List<String> allPermissions;
}
