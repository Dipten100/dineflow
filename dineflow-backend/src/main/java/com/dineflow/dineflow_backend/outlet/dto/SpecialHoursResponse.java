package com.dineflow.dineflow_backend.outlet.dto;

public record SpecialHoursResponse(
    Long id,
    Long outletId,
    String date,
    String startTime,
    String endTime,
    String reason,
    Boolean isClosed
) {
    
}
