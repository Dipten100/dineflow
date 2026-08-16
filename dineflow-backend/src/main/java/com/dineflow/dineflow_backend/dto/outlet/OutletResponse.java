package com.dineflow.dineflow_backend.dto.outlet;

import com.dineflow.dineflow_backend.entity.enums.OutletStatus;

public record OutletResponse(
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
        OutletStatus status
) {
}
