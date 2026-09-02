package com.dineflow.dineflow_backend.restaurant.service;

import com.dineflow.dineflow_backend.outlet.dto.OutletResponseDetails;
import com.dineflow.dineflow_backend.outlet.dto.RegularHoursResponse;
import com.dineflow.dineflow_backend.outlet.dto.SpecialHoursResponse;
import com.dineflow.dineflow_backend.restaurant.dto.CreateRestaurantRequest;
import com.dineflow.dineflow_backend.restaurant.dto.RestaurantResponse;
import com.dineflow.dineflow_backend.restaurant.dto.RestaurantResponseWithOutlets;
import com.dineflow.dineflow_backend.restaurant.dto.RestaurantResponseWithOutletsDetails;
import com.dineflow.dineflow_backend.restaurant.entity.Restaurant;
import com.dineflow.dineflow_backend.restaurant.entity.enums.RestaurantStatus;
import com.dineflow.dineflow_backend.restaurant.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Restaurant create(
            CreateRestaurantRequest request
    ) {

        if (restaurantRepository.existsByName(
                request.getName()
        )) {

            throw new IllegalArgumentException(
                    "Restaurant already exists"
            );
        }

        Restaurant restaurant =
                Restaurant.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .email(request.getEmail())
                        .phone(request.getPhone())
                        .status(RestaurantStatus.ACTIVE)
                        .build();

        return restaurantRepository.save(
                restaurant
        );
    }


    public List<Restaurant> findAll() {
        // all restaurants only (without outlets)
        return restaurantRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponseWithOutlets> findAllWithOutlets() {
        List<Restaurant> restaurants = restaurantRepository.findAllWithOutlets();
        return restaurants.stream()
                .map(r -> new RestaurantResponseWithOutlets(
                        r.getId(),
                        r.getName(),
                        r.getDescription(),
                        r.getEmail(),
                        r.getPhone(),
                        r.getStatus(),
                        r.getOutlets().stream()
                                .map(o -> new com.dineflow.dineflow_backend.outlet.dto.OutletResponse(
                                        o.getId(),
                                        o.getRestaurant().getId(),
                                        o.getName(),
                                        o.getCode(),
                                        o.getAddressLine1(),
                                        o.getAddressLine2(),
                                        o.getCity(),
                                        o.getState(),
                                        o.getPostalCode(),
                                        o.getPhone(),
                                        o.getTimeZone(),
                                        o.getStatus()
                                ))
                                .toList(),
                        r.getCreatedAt(),
                        r.getUpdatedAt()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public RestaurantResponseWithOutletsDetails getById(Long id) {
        return restaurantRepository.findByIdWithOutlets(id)
                .map(r -> new RestaurantResponseWithOutletsDetails(
                        r.getId(),
                        r.getName(),
                        r.getDescription(),
                        r.getEmail(),
                        r.getPhone(),
                        r.getStatus(),
                        r.getOutlets().stream()
                                .map(o -> new OutletResponseDetails(
                                        o.getId(),
                                        o.getRestaurant().getId(),
                                        o.getName(),
                                        o.getCode(),
                                        o.getAddressLine1(),
                                        o.getAddressLine2(),
                                        o.getCity(),
                                        o.getState(),
                                        o.getPostalCode(),
                                        o.getPhone(),
                                        o.getTimeZone(),
                                        o.getStatus(),
                                        o.getRegularHours() != null ? o.getRegularHours().stream()
                                                .map(rh -> new RegularHoursResponse(
                                                        rh.getId(),
                                                        rh.getOutlet().getId(),
                                                        rh.getDayOfWeek().toString(),
                                                        rh.getOpenTime() != null ? rh.getOpenTime().toString() : null,
                                                        rh.getCloseTime() != null ? rh.getCloseTime().toString() : null,
                                                        rh.isClosed()
                                                ))
                                                .toList() : List.of(),
                                        o.getSpecialHours() != null ? o.getSpecialHours().stream()
                                                .map(sh -> new SpecialHoursResponse(
                                                        sh.getId(),
                                                        sh.getOutlet().getId(),
                                                        sh.getDate().toString(),
                                                        sh.getOpenTime() != null ? sh.getOpenTime().toString() : null,
                                                        sh.getCloseTime() != null ? sh.getCloseTime().toString() : null,
                                                        sh.getReason(),
                                                        sh.isClosed()
                                                ))
                                                .toList() : List.of()
                                ))
                                .toList(),
                        r.getCreatedAt(),
                        r.getUpdatedAt()
                ))
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> findAllWithCount() {
        List<Object[]> results = restaurantRepository.findAllWithCountRaw();
        return results.stream()
                .map(row -> new RestaurantResponse(
                        (Long) row[0],
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        (String) row[4],
                        (com.dineflow.dineflow_backend.restaurant.entity.enums.RestaurantStatus) row[5],
                        ((Number) row[6]).intValue(),
                        (java.time.LocalDateTime) row[7],
                        (java.time.LocalDateTime) row[8]
                ))
                .toList();
    }
}
