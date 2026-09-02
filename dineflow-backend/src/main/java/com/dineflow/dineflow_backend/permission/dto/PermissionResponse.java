package com.dineflow.dineflow_backend.permission.dto;

import java.util.List;

public record PermissionResponse(
    PermissionResponseSummary summary,
    PermissionResponsePagination pagination,
    List<PermissionResponseDetail> permissionDetails
) {
    
}
