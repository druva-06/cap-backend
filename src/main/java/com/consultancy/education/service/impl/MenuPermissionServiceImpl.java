package com.consultancy.education.service.impl;

import com.consultancy.education.DTOs.requestDTOs.menupermission.MenuPermissionRequestDto;
import com.consultancy.education.DTOs.responseDTOs.menupermission.MenuPermissionMappingDto;
import com.consultancy.education.DTOs.responseDTOs.menupermission.MenuPermissionResponseDto;
import com.consultancy.education.DTOs.responseDTOs.permission.PermissionResponseDto;
import com.consultancy.education.exception.BadRequestException;
import com.consultancy.education.exception.NotFoundException;
import com.consultancy.education.model.MenuPermission;
import com.consultancy.education.model.Permission;
import com.consultancy.education.repository.MenuPermissionRepository;
import com.consultancy.education.repository.PermissionRepository;
import com.consultancy.education.service.MenuPermissionService;
import com.consultancy.education.transformer.PermissionTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MenuPermissionServiceImpl implements MenuPermissionService {

    @Autowired
    private MenuPermissionRepository menuPermissionRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<MenuPermissionResponseDto> getAllMenuPermissions() {
        log.info("Fetching all menu permissions");
        List<MenuPermission> menuPermissions = menuPermissionRepository.findAll();
        return menuPermissions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuPermissionResponseDto> getMenuPermissions(String menuId) {
        log.info("Fetching permissions for menu: {}", menuId);
        List<MenuPermission> menuPermissions = menuPermissionRepository.findByMenuId(menuId);
        return menuPermissions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuPermissionResponseDto> getSubmenuPermissions(String menuId, String submenuId) {
        log.info("Fetching permissions for menu: {}, submenu: {}", menuId, submenuId);
        List<MenuPermission> menuPermissions = menuPermissionRepository.findByMenuIdAndSubmenuId(menuId, submenuId);
        return menuPermissions.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<String>> getMenuPermissionMappings() {
        log.info("Fetching menu permission mappings");

        List<MenuPermission> allMenuPermissions = menuPermissionRepository.findAll();
        Map<String, List<String>> mappings = new HashMap<>();

        for (MenuPermission mp : allMenuPermissions) {
            String key = mp.getMenuId() + (mp.getSubmenuId() != null ? ":" + mp.getSubmenuId() : "");

            mappings.computeIfAbsent(key, k -> new ArrayList<>())
                    .add(mp.getPermission().getName());
        }

        return mappings;
    }

    @Override
    public List<MenuPermissionMappingDto> getAllMenuStructure() {
        log.info("Fetching all menu structure with permissions");

        List<MenuPermission> allMenuPermissions = menuPermissionRepository.findAll();

        // Group by menu and submenu
        Map<String, Map<String, List<String>>> grouped = new HashMap<>();

        for (MenuPermission mp : allMenuPermissions) {
            String menuId = mp.getMenuId();
            String submenuId = mp.getSubmenuId() != null ? mp.getSubmenuId() : "__main__";

            grouped.computeIfAbsent(menuId, k -> new HashMap<>())
                    .computeIfAbsent(submenuId, k -> new ArrayList<>())
                    .add(mp.getPermission().getName());
        }

        // Convert to DTOs
        List<MenuPermissionMappingDto> result = new ArrayList<>();

        for (Map.Entry<String, Map<String, List<String>>> menuEntry : grouped.entrySet()) {
            String menuId = menuEntry.getKey();

            for (Map.Entry<String, List<String>> submenuEntry : menuEntry.getValue().entrySet()) {
                String submenuId = submenuEntry.getKey();
                List<String> permissions = submenuEntry.getValue();

                result.add(MenuPermissionMappingDto.builder()
                        .menuId(menuId)
                        .submenuId(submenuId.equals("__main__") ? null : submenuId)
                        .permissionNames(permissions)
                        .build());
            }
        }

        // Sort by menuId, then submenuId
        result.sort(Comparator.comparing(MenuPermissionMappingDto::getMenuId)
                .thenComparing(dto -> dto.getSubmenuId() != null ? dto.getSubmenuId() : ""));

        return result;
    }

    @Override
    @Transactional
    public void updateMenuPermissions(MenuPermissionRequestDto request) {
        log.info("Updating permissions for menu: {}, submenu: {}", request.getMenuId(), request.getSubmenuId());

        // Validate permissions exist
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissionIds());
        if (permissions.size() != request.getPermissionIds().size()) {
            throw new BadRequestException("One or more permission IDs are invalid");
        }

        // Delete existing permissions for this menu/submenu
        removeAllMenuPermissions(request.getMenuId(), request.getSubmenuId());

        // Add new permissions
        for (Permission permission : permissions) {
            MenuPermission menuPermission = MenuPermission.builder()
                    .menuId(request.getMenuId())
                    .submenuId(request.getSubmenuId())
                    .permission(permission)
                    .build();
            menuPermissionRepository.save(menuPermission);
        }

        log.info("Successfully updated {} permissions for menu: {}, submenu: {}",
                permissions.size(), request.getMenuId(), request.getSubmenuId());
    }

    @Override
    @Transactional
    public void addMenuPermissions(MenuPermissionRequestDto request) {
        log.info("Adding permissions to menu: {}, submenu: {}", request.getMenuId(), request.getSubmenuId());

        // Validate permissions exist
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissionIds());
        if (permissions.size() != request.getPermissionIds().size()) {
            throw new BadRequestException("One or more permission IDs are invalid");
        }

        // Get existing permissions
        List<MenuPermission> existing = menuPermissionRepository
                .findByMenuIdAndSubmenuId(request.getMenuId(), request.getSubmenuId());

        Set<Long> existingPermissionIds = existing.stream()
                .map(mp -> mp.getPermission().getId())
                .collect(Collectors.toSet());

        // Add only new permissions (avoid duplicates)
        int added = 0;
        for (Permission permission : permissions) {
            if (!existingPermissionIds.contains(permission.getId())) {
                MenuPermission menuPermission = MenuPermission.builder()
                        .menuId(request.getMenuId())
                        .submenuId(request.getSubmenuId())
                        .permission(permission)
                        .build();
                menuPermissionRepository.save(menuPermission);
                added++;
            }
        }

        log.info("Added {} new permissions to menu: {}, submenu: {}",
                added, request.getMenuId(), request.getSubmenuId());
    }

    @Override
    @Transactional
    public void removeMenuPermission(String menuId, String submenuId, Long permissionId) {
        log.info("Removing permission {} from menu: {}, submenu: {}", permissionId, menuId, submenuId);

        List<MenuPermission> menuPermissions = menuPermissionRepository
                .findByMenuIdAndSubmenuId(menuId, submenuId);

        Optional<MenuPermission> toRemove = menuPermissions.stream()
                .filter(mp -> mp.getPermission().getId().equals(permissionId))
                .findFirst();

        if (toRemove.isPresent()) {
            menuPermissionRepository.delete(toRemove.get());
            log.info("Successfully removed permission {} from menu: {}, submenu: {}",
                    permissionId, menuId, submenuId);
        } else {
            throw new NotFoundException("Menu permission not found");
        }
    }

    @Override
    @Transactional
    public void removeAllMenuPermissions(String menuId, String submenuId) {
        log.info("Removing all permissions from menu: {}, submenu: {}", menuId, submenuId);
        menuPermissionRepository.deleteByMenuIdAndSubmenuId(menuId, submenuId);
    }

    /**
     * Convert MenuPermission entity to DTO
     */
    private MenuPermissionResponseDto toDto(MenuPermission menuPermission) {
        PermissionResponseDto permissionDto = PermissionTransformer
                .permissionToResponseDto(menuPermission.getPermission());

        return MenuPermissionResponseDto.builder()
                .id(menuPermission.getId())
                .menuId(menuPermission.getMenuId())
                .submenuId(menuPermission.getSubmenuId())
                .permission(permissionDto)
                .createdAt(menuPermission.getCreatedAt())
                .updatedAt(menuPermission.getUpdatedAt())
                .build();
    }
}
