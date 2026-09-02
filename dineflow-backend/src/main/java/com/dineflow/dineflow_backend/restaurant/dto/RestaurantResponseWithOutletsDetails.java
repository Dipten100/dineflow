package com.dineflow.dineflow_backend.restaurant.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.dineflow.dineflow_backend.outlet.dto.OutletResponseDetails;
import com.dineflow.dineflow_backend.restaurant.entity.enums.RestaurantStatus;

public record RestaurantResponseWithOutletsDetails(
        Long id,
        String name,
        String description,
        String email,
        String phone,
        RestaurantStatus status,
        List<OutletResponseDetails> outlets,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}