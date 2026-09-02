package com.dineflow.dineflow_backend.permission.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.dineflow.dineflow_backend.permission.dto.CreatePermissionRequest;
import com.dineflow.dineflow_backend.permission.dto.PermissionResponse;
import com.dineflow.dineflow_backend.permission.dto.PermissionResponseDetail;
import com.dineflow.dineflow_backend.permission.dto.PermissionResponsePagination;
import com.dineflow.dineflow_backend.permission.dto.PermissionResponseSummary;
import com.dineflow.dineflow_backend.permission.entity.Permission;
import com.dineflow.dineflow_backend.permission.entity.Role;
import com.dineflow.dineflow_backend.permission.entity.RolePermission;
import com.dineflow.dineflow_backend.permission.repository.PermissionRepository;
import com.dineflow.dineflow_backend.permission.repository.RolePermissionRepository;
import com.dineflow.dineflow_backend.permission.repository.RoleRepository;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PermissionsService {
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public PermissionResponseDetail createPermission(CreatePermissionRequest request) {
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
        return new PermissionResponseDetail(
                savedPermission.getId(),
                savedPermission.getName(),
                savedPermission.getDescription(),
                savedPermission.getModule(),
                savedPermission.getAction());
    }

    public PermissionResponse getAllPermissions(Integer page, Integer size, String search) {
        Pageable pageable = (page == null || size == null)
                ? Pageable.unpaged()
                : PageRequest.of(page - 1, size, Sort.by("id").ascending());

        boolean hasSearch = search != null && !search.isBlank();

        Page<Permission> permissions = hasSearch
                ? permissionRepository.searchByKeyword(search.trim(), pageable)
                : permissionRepository.findAll(pageable);

        // Fetch ALL matching rows (unpaged) purely for summary counts
        List<Permission> allMatching = hasSearch
                ? permissionRepository.searchByKeyword(search.trim(), Pageable.unpaged()).getContent()
                : permissionRepository.findAll();

        PermissionResponseSummary summary = new PermissionResponseSummary(
                (long) allMatching.stream().map(Permission::getModule).distinct().count(),
                (long) allMatching.size(),
                (long) allMatching.stream().map(Permission::getAction).distinct().count());

        PermissionResponsePagination pagination = new PermissionResponsePagination(
                permissions.getNumber(),
                permissions.getSize(),
                permissions.getTotalElements(),
                permissions.getTotalPages(),
                permissions.isLast());

        List<PermissionResponseDetail> permissionDetails = permissions.map(permission -> new PermissionResponseDetail(
                permission.getId(),
                permission.getName(),
                permission.getDescription(),
                permission.getModule(),
                permission.getAction()))
                .getContent();

        return new PermissionResponse(summary, pagination, permissionDetails);
    }
}
