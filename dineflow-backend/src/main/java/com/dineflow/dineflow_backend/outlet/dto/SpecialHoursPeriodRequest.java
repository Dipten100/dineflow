package com.dineflow.dineflow_backend.outlet.dto;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record SpecialHoursPeriodRequest (

        @NotNull
        LocalTime openTime,

        @NotNull
        LocalTime closeTime
) {
    
}
