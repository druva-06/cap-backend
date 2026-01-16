package com.consultancy.education.controller;

import com.consultancy.education.DTOs.requestDTOs.menupermission.MenuPermissionRequestDto;
import com.consultancy.education.DTOs.responseDTOs.menupermission.MenuPermissionMappingDto;
import com.consultancy.education.DTOs.responseDTOs.menupermission.MenuPermissionResponseDto;
import com.consultancy.education.exception.BadRequestException;
import com.consultancy.education.exception.NotFoundException;
import com.consultancy.education.response.ApiFailureResponse;
import com.consultancy.education.response.ApiSuccessResponse;
import com.consultancy.education.service.MenuPermissionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("menu-permissions")
public class MenuPermissionController {

    @Autowired
    private MenuPermissionService menuPermissionService;

    /**
     * Get all menu permissions
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllMenuPermissions() {
        try {
            List<MenuPermissionResponseDto> menuPermissions = menuPermissionService.getAllMenuPermissions();
            return ResponseEntity.ok()
                    .body(new ApiSuccessResponse<>(menuPermissions,
                            "Menu permissions retrieved successfully", 200));
        } catch (Exception e) {
            log.error("Error retrieving menu permissions: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(),
                            "Error retrieving menu permissions", 500));
        }
    }

    /**
     * Get menu permission mappings
     * Returns Map<"menuId:submenuId", List<permissionNames>>
     */
    @GetMapping("/mappings")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMenuPermissionMappings() {
        try {
            Map<String, List<String>> mappings = menuPermissionService.getMenuPermissionMappings();
            return ResponseEntity.ok()
                    .body(new ApiSuccessResponse<>(mappings,
                            "Menu permission mappings retrieved successfully", 200));
        } catch (Exception e) {
            log.error("Error retrieving menu permission mappings: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(),
                            "Error retrieving menu permission mappings", 500));
        }
    }

    /**
     * Get all menu structure with permissions
     */
    @GetMapping("/structure")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllMenuStructure() {
        try {
            List<MenuPermissionMappingDto> structure = menuPermissionService.getAllMenuStructure();
            return ResponseEntity.ok()
                    .body(new ApiSuccessResponse<>(structure,
                            "Menu structure retrieved successfully", 200));
        } catch (Exception e) {
            log.error("Error retrieving menu structure: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(),
                            "Error retrieving menu structure", 500));
        }
    }

    /**
     * Get permissions for a specific menu
     */
    @GetMapping("/{menuId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getMenuPermissions(@PathVariable String menuId) {
        try {
            List<MenuPermissionResponseDto> permissions = menuPermissionService.getMenuPermissions(menuId);
            return ResponseEntity.ok()
                    .body(new ApiSuccessResponse<>(permissions,
                            "Menu permissions retrieved successfully", 200));
        } catch (Exception e) {
            log.error("Error retrieving permissions for menu {}: ", menuId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(),
                            "Error retrieving menu permissions", 500));
        }
    }

    /**
     * Get permissions for a specific submenu
     */
    @GetMapping("/{menuId}/{submenuId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSubmenuPermissions(
            @PathVariable String menuId,
            @PathVariable String submenuId) {
        try {
            List<MenuPermissionResponseDto> permissions = menuPermissionService.getSubmenuPermissions(menuId,
                    submenuId);
            return ResponseEntity.ok()
                    .body(new ApiSuccessResponse<>(permissions,
                            "Submenu permissions retrieved successfully", 200));
        } catch (Exception e) {
            log.error("Error retrieving permissions for submenu {}/{}: ", menuId, submenuId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(),
                            "Error retrieving submenu permissions", 500));
        }
    }

    /**
     * Update permissions for a menu/submenu (replaces existing)
     */
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateMenuPermissions(@Valid @RequestBody MenuPermissionRequestDto request) {
        try {
            menuPermissionService.updateMenuPermissions(request);
            return ResponseEntity.ok()
                    .body(new ApiSuccessResponse<>(null,
                            "Menu permissions updated successfully", 200));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 400));
        } catch (Exception e) {
            log.error("Error updating menu permissions: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(),
                            "Error updating menu permissions", 500));
        }
    }

    /**
     * Add permissions to a menu/submenu (keeps existing)
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addMenuPermissions(@Valid @RequestBody MenuPermissionRequestDto request) {
        try {
            menuPermissionService.addMenuPermissions(request);
            return ResponseEntity.ok()
                    .body(new ApiSuccessResponse<>(null,
                            "Permissions added successfully", 200));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 400));
        } catch (Exception e) {
            log.error("Error adding menu permissions: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(),
                            "Error adding menu permissions", 500));
        }
    }

    /**
     * Remove specific permission from menu/submenu
     */
    @DeleteMapping("/{menuId}/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeMenuPermission(
            @PathVariable String menuId,
            @PathVariable Long permissionId,
            @RequestParam(required = false) String submenuId) {
        try {
            menuPermissionService.removeMenuPermission(menuId, submenuId, permissionId);
            return ResponseEntity.ok()
                    .body(new ApiSuccessResponse<>(null,
                            "Permission removed successfully", 200));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        } catch (Exception e) {
            log.error("Error removing menu permission: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(),
                            "Error removing menu permission", 500));
        }
    }

    /**
     * Remove all permissions from menu/submenu
     */
    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeAllMenuPermissions(
            @PathVariable String menuId,
            @RequestParam(required = false) String submenuId) {
        try {
            menuPermissionService.removeAllMenuPermissions(menuId, submenuId);
            return ResponseEntity.ok()
                    .body(new ApiSuccessResponse<>(null,
                            "All permissions removed successfully", 200));
        } catch (Exception e) {
            log.error("Error removing all menu permissions: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(),
                            "Error removing all menu permissions", 500));
        }
    }
}
