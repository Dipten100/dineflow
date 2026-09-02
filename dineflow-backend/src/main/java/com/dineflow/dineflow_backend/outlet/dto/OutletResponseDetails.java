package com.dineflow.dineflow_backend.outlet.dto;

import java.util.List;

import com.dineflow.dineflow_backend.outlet.entity.enums.OutletStatus;

public record OutletResponseDetails(
        Long id,
        Long restaurantId,
        String name,
        String code,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String postalCode,
        String phone,
        String timeZone,
        OutletStatus status,
        List<RegularHoursResponse> regularHours,
        List<SpecialHoursResponse> specialHours
) {
}
