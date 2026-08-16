package com.dineflow.dineflow_backend.dto.user;

public record UserOutletResponse(
        Long userId,
        Long outletId,
        String outletName
) {
}
