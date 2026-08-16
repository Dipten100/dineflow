package com.dineflow.dineflow_backend.service;

import com.dineflow.dineflow_backend.dto.outlet.CreateOutletRequest;
import com.dineflow.dineflow_backend.dto.outlet.OutletResponse;
import com.dineflow.dineflow_backend.dto.outlet.OutletResponseDetails;
import com.dineflow.dineflow_backend.dto.outlet.RegularHoursResponse;
import com.dineflow.dineflow_backend.dto.outlet.SpecialHoursResponse;
import com.dineflow.dineflow_backend.entity.Outlet;
import com.dineflow.dineflow_backend.entity.OutletRegularHours;
import com.dineflow.dineflow_backend.entity.OutletSpecialHours;
import com.dineflow.dineflow_backend.entity.Restaurant;
import com.dineflow.dineflow_backend.entity.enums.OutletStatus;
import com.dineflow.dineflow_backend.repository.OutletRepository;
import com.dineflow.dineflow_backend.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutletService {

    private final OutletRepository outletRepository;

    private final RestaurantRepository restaurantRepository;


    @Transactional
    public OutletResponse create(
            Long restaurantId,
            CreateOutletRequest request
    ) {

        Restaurant restaurant =
                restaurantRepository.findById(restaurantId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Restaurant not found"
                                )
                        );

        if (outletRepository.existsByCode(
                request.getCode()
        )) {

            throw new IllegalArgumentException(
                    "Outlet code already exists"
            );
        }

        if (outletRepository
                .existsByRestaurantIdAndName(
                        restaurantId,
                        request.getName()
                )) {

            throw new IllegalArgumentException(
                    "Outlet already exists in this restaurant"
            );
        }

        Outlet outlet = Outlet.builder()
                .restaurant(restaurant)
                .name(request.getName())
                .code(request.getCode())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .phone(request.getPhone())
                .timeZone(
                        request.getTimeZone() == null ||
                        request.getTimeZone().isBlank()
                                ? "Asia/Kolkata"
                                : request.getTimeZone()
                )
                .status(OutletStatus.ACTIVE)
                .build();

        outlet = outletRepository.save(outlet);

        return toResponse(outlet);
    }


    @Transactional(readOnly = true)
    public List<OutletResponse> findByRestaurant(
            Long restaurantId
    ) {

        if (!restaurantRepository.existsById(
                restaurantId
        )) {
            throw new IllegalArgumentException(
                    "Restaurant not found"
            );
        }

        return outletRepository
                .findByRestaurantId(restaurantId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OutletResponse> findAll() {
        return outletRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OutletResponseDetails findById(Long id) {
        Outlet outlet =
                outletRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Outlet not found"
                                )
                        );

        List<RegularHoursResponse> regularHoursResponses =
                outlet.getRegularHours() != null
                        ? outlet.getRegularHours().stream()
                                .map(this::mapToRegularHoursResponse)
                                .toList()
                        : List.of();

        List<SpecialHoursResponse> specialHoursResponses =
                outlet.getSpecialHours() != null
                        ? outlet.getSpecialHours().stream()
                                .map(this::mapToSpecialHoursResponse)
                                .toList()
                        : List.of();

        return new OutletResponseDetails(
                outlet.getId(),
                outlet.getRestaurant().getId(),
                outlet.getName(),
                outlet.getCode(),
                outlet.getAddressLine1(),
                outlet.getAddressLine2(),
                outlet.getCity(),
                outlet.getState(),
                outlet.getPostalCode(),
                outlet.getPhone(),
                outlet.getTimeZone(),
                outlet.getStatus(),
                regularHoursResponses,
                specialHoursResponses
        );
    }

    private RegularHoursResponse mapToRegularHoursResponse(OutletRegularHours hours) {
        return new RegularHoursResponse(
                hours.getId(),
                hours.getOutlet().getId(),
                hours.getDayOfWeek().toString(),
                hours.getOpenTime() != null ? hours.getOpenTime().toString() : null,
                hours.getCloseTime() != null ? hours.getCloseTime().toString() : null,
                hours.isClosed()
        );
    }

    private SpecialHoursResponse mapToSpecialHoursResponse(OutletSpecialHours hours) {
        return new SpecialHoursResponse(
                hours.getId(),
                hours.getOutlet().getId(),
                hours.getDate() != null ? hours.getDate().toString() : null,
                hours.getOpenTime() != null ? hours.getOpenTime().toString() : null,
                hours.getCloseTime() != null ? hours.getCloseTime().toString() : null,
                hours.getReason(),
                hours.isClosed()
        );
    }


    @Transactional(readOnly = true)
    public OutletResponse findByIdByRestaurantId(
            Long restaurantId,
            Long id
    ) {

        if (!restaurantRepository.existsById(
                restaurantId
        )) {
            throw new IllegalArgumentException(
                    "Restaurant not found"
            );
        }

        Outlet outlet =
                outletRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Outlet not found"
                                )
                        );

        if (!outlet.getRestaurant()
                .getId()
                .equals(restaurantId)) {
            throw new IllegalArgumentException(
                    "Outlet does not belong to this restaurant"
            );
        }

        return toResponse(outlet);
    }


    private OutletResponse toResponse(
            Outlet outlet
    ) {

        return new OutletResponse(
                outlet.getId(),
                outlet.getRestaurant().getId(),
                outlet.getName(),
                outlet.getCode(),
                outlet.getAddressLine1(),
                outlet.getAddressLine2(),
                outlet.getCity(),
                outlet.getState(),
                outlet.getPostalCode(),
                outlet.getPhone(),
                outlet.getTimeZone(),
                outlet.getStatus()
        );
    }
}
