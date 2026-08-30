package com.dineflow.dineflow_backend.dto.permission;

public record PermissionResponseDetail(
    Long id,
    String name,
    String description,
    String module,
    String action
) {
    
}
