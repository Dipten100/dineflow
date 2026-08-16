package com.dineflow.dineflow_backend.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dineflow.dineflow_backend.dto.ApiResponse;
import com.dineflow.dineflow_backend.dto.permission.CreatePermissionRequest;
import com.dineflow.dineflow_backend.dto.permission.PermissionResponse;
import com.dineflow.dineflow_backend.service.PermissionsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionsService permissionsService;

    @PostMapping
    @PreAuthorize("@permissionService.hasPermission(authentication, 'PERMISSION_CREATE')")
    public ResponseEntity<ApiResponse<PermissionResponse>> createPermission(
            @Valid @RequestBody CreatePermissionRequest request) {
        PermissionResponse response = permissionsService.createPermission(request);
        return ResponseEntity.ok(ApiResponse.success("Permission created successfully", response));
    }

    @GetMapping
    @PreAuthorize("@permissionService.hasPermission(authentication, 'PERMISSION_VIEW')")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAllPermissions() {
        List<PermissionResponse> response = permissionsService.getAllPermissions();
        return ResponseEntity.ok(ApiResponse.success("Permissions retrieved successfully", response));
    }
}
