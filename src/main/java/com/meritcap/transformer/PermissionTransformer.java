package com.meritcap.transformer;

import com.meritcap.DTOs.requestDTOs.permission.PermissionRequestDto;
import com.meritcap.DTOs.responseDTOs.permission.PermissionResponseDto;
import com.meritcap.model.Permission;

public class PermissionTransformer {

    public static Permission requestDtoToPermission(PermissionRequestDto requestDto) {
        return Permission.builder()
                .name(requestDto.getName().toUpperCase())
                .displayName(requestDto.getDisplayName())
                .description(requestDto.getDescription())
                .category(requestDto.getCategory() != null ? requestDto.getCategory().toUpperCase() : null)
                .isActive(requestDto.getIsActive() != null ? requestDto.getIsActive() : true)
                .build();
    }

    public static PermissionResponseDto permissionToResponseDto(Permission permission) {
        return PermissionResponseDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .displayName(permission.getDisplayName())
                .description(permission.getDescription())
                .category(permission.getCategory())
                .dashboard(permission.getDashboard())
                .submenu(permission.getSubmenu())
                .feature(permission.getFeature())
                .isActive(permission.getIsActive())
                .createdAt(permission.getCreatedAt())
                .updatedAt(permission.getUpdatedAt())
                .build();
    }

    public static void updatePermissionFromDto(Permission permission, PermissionRequestDto requestDto) {
        if (requestDto.getName() != null) {
            permission.setName(requestDto.getName().toUpperCase());
        }
        if (requestDto.getDisplayName() != null) {
            permission.setDisplayName(requestDto.getDisplayName());
        }
        if (requestDto.getDescription() != null) {
            permission.setDescription(requestDto.getDescription());
        }
        if (requestDto.getCategory() != null) {
            permission.setCategory(requestDto.getCategory().toUpperCase());
        }
        if (requestDto.getIsActive() != null) {
            permission.setIsActive(requestDto.getIsActive());
        }
    }
}
