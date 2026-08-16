package com.dineflow.dineflow_backend.dto.outlet;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record RegularHoursPeriodRequest(

        @NotNull
        LocalTime openTime,

        @NotNull
        LocalTime closeTime
) {
}
