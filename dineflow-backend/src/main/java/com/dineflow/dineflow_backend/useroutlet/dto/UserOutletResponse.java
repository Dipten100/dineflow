package com.dineflow.dineflow_backend.useroutlet.dto;

public record UserOutletResponse(
        Long userId,
        Long outletId,
        String outletName
) {
}
