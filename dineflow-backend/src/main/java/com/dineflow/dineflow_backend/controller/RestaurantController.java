package com.dineflow.dineflow_backend.controller;

import com.dineflow.dineflow_backend.dto.ApiResponse;
import com.dineflow.dineflow_backend.dto.restaurant.CreateRestaurantRequest;
import com.dineflow.dineflow_backend.dto.restaurant.RestaurantResponse;
import com.dineflow.dineflow_backend.dto.restaurant.RestaurantResponseWithOutlets;
import com.dineflow.dineflow_backend.dto.restaurant.RestaurantResponseWithOutletsDetails;
import com.dineflow.dineflow_backend.entity.Restaurant;
import com.dineflow.dineflow_backend.service.RestaurantService;

import jakarta.validation.Valid;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    @PreAuthorize(
            "@permissionService.hasPermission(authentication, 'RESTAURANT_CREATE')"
    )
    public ResponseEntity<ApiResponse<Restaurant>> create(
            @Valid @RequestBody CreateRestaurantRequest request
    ) {

        Restaurant restaurant =
                restaurantService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Restaurant created successfully",
                                restaurant
                        )
                );
    }
    
    @GetMapping
    @PreAuthorize(
            "@permissionService.hasPermission(authentication, 'RESTAURANT_VIEW')"
    )
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> getAll() {
        List<RestaurantResponse> response = restaurantService.findAllWithCount();
        return ResponseEntity
                .ok(
                        ApiResponse.success(
                                "Restaurants retrieved successfully",
                                response
                        )
                );
    }

    @GetMapping("/details")
    @PreAuthorize(
            "@permissionService.hasPermission(authentication, 'RESTAURANT_VIEW')"
    )
    public ResponseEntity<ApiResponse<List<RestaurantResponseWithOutlets>>> getAllDetails() {
        List<RestaurantResponseWithOutlets> response = restaurantService.findAllWithOutlets();
        return ResponseEntity
                .ok(
                        ApiResponse.success(
                                "Restaurants retrieved successfully",
                                response
                        )
                );
    }
    
    @GetMapping("/details/{id}")
    @PreAuthorize(
            "@permissionService.hasPermission(authentication, 'RESTAURANT_VIEW')"
    )
    public ResponseEntity<ApiResponse<RestaurantResponseWithOutletsDetails>> getDetailsById(@PathVariable Long id) {
        RestaurantResponseWithOutletsDetails response = restaurantService.getById(id);
        return ResponseEntity
                .ok(
                        ApiResponse.success(
                                "Restaurant retrieved successfully",
                                response
                        )
                );
    }
}
