package com.meritcap.DTOs.responseDTOs.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItemDto {
    private String id;
    private String label;
    private String icon;
    private String href;
    private Boolean visible;
    private List<String> requiredPermissions;
    private List<SubMenuItemDto> submenu;
    private Map<String, Boolean> features;
    private String roleSpecificView;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SubMenuItemDto {
        private String id;
        private String label;
        private String href;
        private Boolean visible;
        private List<String> requiredPermissions;
    }
}
