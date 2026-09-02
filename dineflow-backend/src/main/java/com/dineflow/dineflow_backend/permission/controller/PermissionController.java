package com.dineflow.dineflow_backend.permission.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dineflow.dineflow_backend.common.dto.ApiResponse;
import com.dineflow.dineflow_backend.permission.dto.CreatePermissionRequest;
import com.dineflow.dineflow_backend.permission.dto.PermissionResponse;
import com.dineflow.dineflow_backend.permission.dto.PermissionResponseDetail;
import com.dineflow.dineflow_backend.permission.service.PermissionsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionsService permissionsService;

    @PostMapping
    @PreAuthorize("@permissionService.hasPermission(authentication, 'PERMISSION_CREATE')")
    public ResponseEntity<ApiResponse<PermissionResponseDetail>> createPermission(
            @Valid @RequestBody CreatePermissionRequest request) {
        PermissionResponseDetail response = permissionsService.createPermission(request);
        return ResponseEntity.ok(ApiResponse.success("Permission created successfully", response));
    }

    @GetMapping
    @PreAuthorize("@permissionService.hasPermission(authentication, 'PERMISSION_VIEW')")
    public ResponseEntity<ApiResponse<PermissionResponse>> getAllPermissions(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String search) {

        PermissionResponse response = permissionsService.getAllPermissions(page, size, search);
        return ResponseEntity.ok(ApiResponse.success(
                "Permissions retrieved successfully",
                response));
    }
}
