package com.dineflow.dineflow_backend.restaurant.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.dineflow.dineflow_backend.outlet.dto.OutletResponse;
import com.dineflow.dineflow_backend.restaurant.entity.enums.RestaurantStatus;

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
