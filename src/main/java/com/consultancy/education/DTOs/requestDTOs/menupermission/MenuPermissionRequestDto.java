package com.consultancy.education.DTOs.requestDTOs.menupermission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuPermissionRequestDto {

    @NotBlank(message = "Menu ID is required")
    private String menuId;

    private String submenuId; // Optional: null for main menu

    @NotEmpty(message = "At least one permission ID is required")
    private List<Long> permissionIds;
}
