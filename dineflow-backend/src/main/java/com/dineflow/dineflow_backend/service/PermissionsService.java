package com.dineflow.dineflow_backend.service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dineflow.dineflow_backend.dto.permission.CreatePermissionRequest;
import com.dineflow.dineflow_backend.dto.permission.PermissionResponse;
import com.dineflow.dineflow_backend.entity.Permission;
import com.dineflow.dineflow_backend.entity.Role;
import com.dineflow.dineflow_backend.entity.RolePermission;
import com.dineflow.dineflow_backend.repository.PermissionRepository;
import com.dineflow.dineflow_backend.repository.RolePermissionRepository;
import com.dineflow.dineflow_backend.repository.RoleRepository;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PermissionsService {
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public PermissionResponse createPermission(CreatePermissionRequest request) {
        // Check if permission already exists
        if (permissionRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Permission already exists");
        }
        
        // Create new permission
        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        permission.setModule(request.getModule());
        permission.setAction(request.getAction());
        
        Permission savedPermission = permissionRepository.save(permission);

        // give access to SUPER_ADMIN
        Role superAdminRole = roleRepository.findByName("SUPER_ADMIN")
                .orElseThrow(() -> new IllegalArgumentException("SUPER_ADMIN role not found"));

        RolePermission rolePermission = RolePermission.builder()
                .role(superAdminRole)
                .permission(savedPermission)
                .build();
        rolePermissionRepository.save(rolePermission);
        
        // Return response
        return new PermissionResponse(
            savedPermission.getId(),
            savedPermission.getName(),
            savedPermission.getDescription(),
            savedPermission.getModule(),
            savedPermission.getAction()
        );
    }

    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(permission -> new PermissionResponse(
                    permission.getId(),
                    permission.getName(),
                    permission.getDescription(),
                    permission.getModule(),
                    permission.getAction()
                ))
                .collect(Collectors.toList());
    }
}
