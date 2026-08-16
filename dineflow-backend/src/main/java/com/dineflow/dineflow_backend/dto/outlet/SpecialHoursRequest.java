package com.dineflow.dineflow_backend.dto.outlet;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record SpecialHoursRequest(
        @NotNull LocalDate date,

        LocalTime openTime,

        LocalTime closeTime,

        Boolean isClosed,

        String reason
) {

}
