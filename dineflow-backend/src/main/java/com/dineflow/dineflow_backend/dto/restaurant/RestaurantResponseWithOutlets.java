package com.dineflow.dineflow_backend.dto.restaurant;

import java.time.LocalDateTime;
import java.util.List;

import com.dineflow.dineflow_backend.dto.outlet.OutletResponse;
import com.dineflow.dineflow_backend.entity.enums.RestaurantStatus;

public record RestaurantResponseWithOutlets(
        Long id,
        String name,
        String description,
        String email,
        String phone,
        RestaurantStatus status,
        List<OutletResponse> outlets,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
