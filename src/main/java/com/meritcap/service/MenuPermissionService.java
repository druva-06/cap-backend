package com.meritcap.service;

import com.meritcap.DTOs.requestDTOs.menupermission.MenuPermissionRequestDto;
import com.meritcap.DTOs.responseDTOs.menupermission.MenuPermissionMappingDto;
import com.meritcap.DTOs.responseDTOs.menupermission.MenuPermissionResponseDto;

import java.util.List;
import java.util.Map;

public interface MenuPermissionService {

    /**
     * Get all menu permissions
     */
    List<MenuPermissionResponseDto> getAllMenuPermissions();

    /**
     * Get permissions for a specific menu
     */
    List<MenuPermissionResponseDto> getMenuPermissions(String menuId);

    /**
     * Get permissions for a specific submenu
     */
    List<MenuPermissionResponseDto> getSubmenuPermissions(String menuId, String submenuId);

    /**
     * Get menu-permission mappings (grouped by menu/submenu)
     * Returns Map<"menuId:submenuId", List<permissionNames>>
     */
    Map<String, List<String>> getMenuPermissionMappings();

    /**
     * Get all menu IDs with their submenu IDs
     */
    List<MenuPermissionMappingDto> getAllMenuStructure();

    /**
     * Update permissions for a menu/submenu
     * Replaces existing permissions with new ones
     */
    void updateMenuPermissions(MenuPermissionRequestDto request);

    /**
     * Add permissions to a menu/submenu
     */
    void addMenuPermissions(MenuPermissionRequestDto request);

    /**
     * Remove specific permission from menu/submenu
     */
    void removeMenuPermission(String menuId, String submenuId, Long permissionId);

    /**
     * Remove all permissions from menu/submenu
     */
    void removeAllMenuPermissions(String menuId, String submenuId);
}
