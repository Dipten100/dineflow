package com.dineflow.dineflow_backend.outlet.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record RegularHoursPeriodRequest(

        @NotNull
        LocalTime openTime,

        @NotNull
        LocalTime closeTime
) {
}
