package com.meritcap.DTOs.responseDTOs.menupermission;

import com.meritcap.DTOs.responseDTOs.permission.PermissionResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuPermissionResponseDto {

    private Long id;
    private String menuId;
    private String submenuId;
    private PermissionResponseDto permission;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
