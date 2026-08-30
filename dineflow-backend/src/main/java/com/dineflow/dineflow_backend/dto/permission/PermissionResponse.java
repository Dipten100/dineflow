package com.dineflow.dineflow_backend.dto.permission;

import java.util.List;

public record PermissionResponse(
    PermissionResponseSummary summary,
    PermissionResponsePagination pagination,
    List<PermissionResponseDetail> permissionDetails
) {
    
}
