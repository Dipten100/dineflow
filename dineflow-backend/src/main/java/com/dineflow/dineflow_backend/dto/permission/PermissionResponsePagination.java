package com.dineflow.dineflow_backend.dto.permission;

public record PermissionResponsePagination(
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean last
) {
}
