package com.consultancy.education.DTOs.responseDTOs.menupermission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuPermissionMappingDto {

    private String menuId;
    private String submenuId;
    private List<String> permissionNames; // List of permission names (e.g., ["MENU_LEADS", "LEAD_VIEW_ALL"])
}
