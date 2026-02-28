package com.meritcap.service;

import com.meritcap.DTOs.requestDTOs.role.RoleRequestDto;
import com.meritcap.DTOs.responseDTOs.role.RoleResponseDto;

import java.util.List;

public interface RoleService {

    RoleResponseDto createRole(RoleRequestDto requestDto, Long createdBy);

    RoleResponseDto updateRole(Long roleId, RoleRequestDto requestDto, Long updatedBy);

    void deleteRole(Long roleId, Long deletedBy);

    RoleResponseDto getRoleById(Long roleId);

    RoleResponseDto getRoleByName(String name);

    List<RoleResponseDto> getAllRoles();

    List<RoleResponseDto> getActiveRoles();
}
