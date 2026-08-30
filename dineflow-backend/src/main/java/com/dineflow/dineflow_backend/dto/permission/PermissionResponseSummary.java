package com.dineflow.dineflow_backend.dto.permission;

public record PermissionResponseSummary(
    Long totalModule,
    Long totalPermission,
    Long totalAction
) {
    
}

