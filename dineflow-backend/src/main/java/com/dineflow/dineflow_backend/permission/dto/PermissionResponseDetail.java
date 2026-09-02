package com.dineflow.dineflow_backend.permission.dto;

public record PermissionResponseDetail(
    Long id,
    String name,
    String description,
    String module,
    String action
) {
    
}
