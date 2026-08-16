package com.dineflow.dineflow_backend.dto.restaurant;

import java.time.LocalDateTime;
import java.util.List;

import com.dineflow.dineflow_backend.dto.outlet.OutletResponseDetails;
import com.dineflow.dineflow_backend.entity.enums.RestaurantStatus;

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