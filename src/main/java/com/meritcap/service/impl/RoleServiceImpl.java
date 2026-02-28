package com.meritcap.service.impl;

import com.meritcap.DTOs.requestDTOs.role.RoleRequestDto;
import com.meritcap.DTOs.responseDTOs.role.RoleResponseDto;
import com.meritcap.exception.AlreadyExistException;
import com.meritcap.exception.BadRequestException;
import com.meritcap.exception.NotFoundException;
import com.meritcap.model.Role;
import com.meritcap.repository.RoleRepository;
import com.meritcap.service.RoleService;
import com.meritcap.transformer.RoleTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public RoleResponseDto createRole(RoleRequestDto requestDto, Long createdBy) {
        log.info("Creating new role: {}", requestDto.getName());

        // Check if role already exists
        if (roleRepository.existsByNameIgnoreCase(requestDto.getName())) {
            throw new AlreadyExistException(List.of("Role with name '" + requestDto.getName() + "' already exists"));
        }

        Role role = RoleTransformer.requestDtoToRole(requestDto);
        role.setCreatedBy(createdBy);
        role.setUpdatedBy(createdBy);

        Role savedRole = roleRepository.save(role);
        log.info("Role created successfully with ID: {}", savedRole.getId());

        return RoleTransformer.roleToResponseDto(savedRole);
    }

    @Override
    @Transactional
    public RoleResponseDto updateRole(Long roleId, RoleRequestDto requestDto, Long updatedBy) {
        log.info("Updating role with ID: {}", roleId);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role not found with ID: " + roleId));

        // Check if new name conflicts with existing role
        if (requestDto.getName() != null && !requestDto.getName().equalsIgnoreCase(role.getName())) {
            if (roleRepository.existsByNameIgnoreCase(requestDto.getName())) {
                throw new AlreadyExistException(
                        List.of("Role with name '" + requestDto.getName() + "' already exists"));
            }
        }

        RoleTransformer.updateRoleFromDto(role, requestDto);
        role.setUpdatedBy(updatedBy);

        Role updatedRole = roleRepository.save(role);
        log.info("Role updated successfully: {}", roleId);

        return RoleTransformer.roleToResponseDto(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId, Long deletedBy) {
        log.info("Deleting role with ID: {}", roleId);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role not found with ID: " + roleId));

        // TODO: Check if any users are assigned this role before deletion
        // Could implement soft delete instead of hard delete

        roleRepository.delete(role);
        log.info("Role deleted successfully: {}", roleId);
    }

    @Override
    public RoleResponseDto getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role not found with ID: " + roleId));
        return RoleTransformer.roleToResponseDto(role);
    }

    @Override
    public RoleResponseDto getRoleByName(String name) {
        Role role = roleRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("Role not found with name: " + name));
        return RoleTransformer.roleToResponseDto(role);
    }

    @Override
    public List<RoleResponseDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(RoleTransformer::roleToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleResponseDto> getActiveRoles() {
        return roleRepository.findByIsActiveTrue().stream()
                .map(RoleTransformer::roleToResponseDto)
                .collect(Collectors.toList());
    }
}
