package com.dineflow.dineflow_backend.outlet.controller;

import com.dineflow.dineflow_backend.common.dto.ApiResponse;
import com.dineflow.dineflow_backend.outlet.dto.CreateOutletRequest;
import com.dineflow.dineflow_backend.outlet.dto.OutletResponse;
import com.dineflow.dineflow_backend.outlet.dto.OutletResponseDetails;
import com.dineflow.dineflow_backend.outlet.dto.RegularHoursRequest;
import com.dineflow.dineflow_backend.outlet.dto.RegularHoursResponse;
import com.dineflow.dineflow_backend.outlet.dto.SpecialHoursRequest;
import com.dineflow.dineflow_backend.outlet.dto.SpecialHoursResponse;
import com.dineflow.dineflow_backend.outlet.service.OutletService;
import com.dineflow.dineflow_backend.outlet.service.RegularHoursService;
import com.dineflow.dineflow_backend.outlet.service.SpecialHoursService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OutletController {

        private final OutletService outletService;

        private final RegularHoursService regularHoursService;

        private final SpecialHoursService specialHoursService;

        @PostMapping("/restaurants/{restaurantId}/outlets")
        @PreAuthorize("@permissionService.hasPermission(authentication, 'OUTLET_CREATE')")
        public ResponseEntity<ApiResponse<OutletResponse>> create(
                        @PathVariable Long restaurantId,

                        @Valid @RequestBody CreateOutletRequest request) {

                OutletResponse response = outletService.create(
                                restaurantId,
                                request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(
                                                ApiResponse.success(
                                                                "Outlet created successfully",
                                                                response));
        }

        @GetMapping("/restaurants/{restaurantId}/outlets")
        @PreAuthorize("@permissionService.hasPermission(authentication, 'OUTLET_VIEW')")
        public ResponseEntity<ApiResponse<List<OutletResponse>>> findByRestaurant(
                        @PathVariable Long restaurantId) {

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Outlets fetched successfully",
                                                outletService.findByRestaurant(
                                                                restaurantId)));
        }

        @GetMapping("/outlets")
        @PreAuthorize("@permissionService.hasPermission(authentication, 'OUTLET_VIEW')")
        public ResponseEntity<ApiResponse<List<OutletResponse>>> findAll() {

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Outlets fetched successfully",
                                                outletService.findAll()));
        }

        @GetMapping("/outlets/{id}")
        @PreAuthorize("@permissionService.hasPermission(authentication, 'OUTLET_VIEW')")
        public ResponseEntity<ApiResponse<OutletResponseDetails>> findById(
                        @PathVariable Long id) {

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Outlet fetched successfully",
                                                outletService.findById(id)));
        }

        @PutMapping("/outlets/{outletId}/hours/regular")
        @PreAuthorize("@permissionService.hasPermission(authentication, 'OUTLET_UPDATE')")
        public ResponseEntity<ApiResponse<Void>> updateRegularHours(
                        @PathVariable Long outletId,

                        @Valid @RequestBody RegularHoursRequest request) {

                regularHoursService.save(
                                outletId,
                                request);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Regular hours updated successfully",
                                                null));
        }

        @GetMapping("/outlets/{outletId}/hours/regular")
        @PreAuthorize("@permissionService.hasPermission(authentication, 'OUTLET_UPDATE')")
        public ResponseEntity<ApiResponse<List<RegularHoursResponse>>> getRegularHours(
                        @PathVariable Long outletId) {

                List<RegularHoursResponse> regularHours = regularHoursService.getRegularHoursByOutletId(outletId);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Regular hours retrieved successfully",
                                                regularHours));
        }

        @PutMapping("/outlets/{outletId}/hours/special")
        @PreAuthorize("@permissionService.hasPermission(authentication, 'OUTLET_UPDATE')")
        public ResponseEntity<ApiResponse<Void>> updateSpecialHours(
                        @PathVariable Long outletId,

                        @Valid @RequestBody SpecialHoursRequest request) {

                specialHoursService.save(
                                outletId,
                                request);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Special hours updated successfully",
                                                null));
        }

        @GetMapping("/outlets/{outletId}/hours/special")
        @PreAuthorize("@permissionService.hasPermission(authentication, 'OUTLET_UPDATE')")
        public ResponseEntity<ApiResponse<List<SpecialHoursResponse>>> getSpecialHours(
                        @PathVariable Long outletId) {

                List<SpecialHoursResponse> specialHours = specialHoursService.getSpecialHoursByOutletId(outletId);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Special hours retrieved successfully",
                                                specialHours));
        }
}
