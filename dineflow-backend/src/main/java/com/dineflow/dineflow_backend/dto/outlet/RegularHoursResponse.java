package com.dineflow.dineflow_backend.dto.outlet;

public record RegularHoursResponse(
    Long id,
    Long outletId,
    String dayOfWeek,
    String startTime,
    String endTime,
    Boolean isClosed
) {
    
}
