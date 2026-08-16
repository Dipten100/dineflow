package com.dineflow.dineflow_backend.controller;

import com.dineflow.dineflow_backend.dto.user.UserOutletResponse;
import com.dineflow.dineflow_backend.service.UserOutletService;
import com.dineflow.dineflow_backend.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserOutletController {

    private final UserOutletService userOutletService;


    @PostMapping(
            "/users/{userId}/outlets/{outletId}"
    )
    @PreAuthorize(
            "@permissionService.hasPermission(" +
            "authentication, 'USER_OUTLET_ASSIGN')"
    )
    public ResponseEntity<
            ApiResponse<UserOutletResponse>
            > assign(

            @PathVariable Long userId,

            @PathVariable Long outletId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User assigned to outlet successfully",
                        userOutletService.assign(
                                userId,
                                outletId
                        )
                )
        );
    }


    @GetMapping(
            "/users/{userId}/outlets"
    )
    @PreAuthorize(
            "@permissionService.hasPermission(" +
            "authentication, 'USER_OUTLET_VIEW')"
    )
    public ResponseEntity<
            ApiResponse<List<UserOutletResponse>>
            > getUserOutlets(

            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User outlets fetched successfully",
                        userOutletService.findByUser(
                                userId
                        )
                )
        );
    }


    @GetMapping(
            "/outlets/{outletId}/users"
    )
    @PreAuthorize(
            "@permissionService.hasPermission(" +
            "authentication, 'USER_OUTLET_VIEW')"
    )
    public ResponseEntity<
            ApiResponse<List<UserOutletResponse>>
            > getOutletUsers(

            @PathVariable Long outletId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Outlet users fetched successfully",
                        userOutletService.findByOutlet(
                                outletId
                        )
                )
        );
    }


    @DeleteMapping(
            "/users/{userId}/outlets/{outletId}"
    )
    @PreAuthorize(
            "@permissionService.hasPermission(" +
            "authentication, 'USER_OUTLET_ASSIGN')"
    )
    public ResponseEntity<
            ApiResponse<Void>
            > remove(

            @PathVariable Long userId,

            @PathVariable Long outletId
    ) {

        userOutletService.remove(
                userId,
                outletId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User removed from outlet successfully",
                        null
                )
        );
    }
}
