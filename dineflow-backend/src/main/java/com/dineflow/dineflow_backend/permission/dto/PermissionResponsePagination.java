package com.dineflow.dineflow_backend.permission.dto;

public record PermissionResponsePagination(
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean last
) {
}
