package com.dineflow.dineflow_backend.dto.permission;

public record PermissionResponse(
    Long id,
    String name,
    String description,
    String module,
    String action
) {
    
}
