package com.dineflow.dineflow_backend.permission.dto;

public record PermissionResponseSummary(
    Long totalModule,
    Long totalPermission,
    Long totalAction
) {
    
}

