package com.dineflow.dineflow_backend.dto.outlet;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record SpecialHoursPeriodRequest (

        @NotNull
        LocalTime openTime,

        @NotNull
        LocalTime closeTime
) {
    
}
