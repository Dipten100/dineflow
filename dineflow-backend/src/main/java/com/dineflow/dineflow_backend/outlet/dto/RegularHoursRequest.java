package com.dineflow.dineflow_backend.outlet.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.List;

public record RegularHoursRequest(

        @NotNull
        DayOfWeek dayOfWeek,

        @NotNull
        @Valid
        List<RegularHoursPeriodRequest> periods
) {
}
