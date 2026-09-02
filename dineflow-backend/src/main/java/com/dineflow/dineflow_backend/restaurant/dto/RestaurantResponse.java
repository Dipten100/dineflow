package com.dineflow.dineflow_backend.restaurant.dto;

import com.dineflow.dineflow_backend.restaurant.entity.enums.RestaurantStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse {
    private Long id;
    private String name;
    private String description;
    private String email;
    private String phone;
    private RestaurantStatus status;
    private Integer outletsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
