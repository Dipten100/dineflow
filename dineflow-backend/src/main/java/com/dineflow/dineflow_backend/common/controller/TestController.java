package com.dineflow.dineflow_backend.common.controller;

import com.dineflow.dineflow_backend.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<String>> publicApi() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Public API",
                        "Anyone can access this"
                )
        );
    }


    @GetMapping("/authenticated")
    public ResponseEntity<ApiResponse<String>> authenticatedApi() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Authenticated API",
                        "You are logged in"
                )
        );
    }


    @PostMapping("/outlet")
    @PreAuthorize(
            "@permissionService.hasPermission(authentication, 'OUTLET_CREATE')"
    )
    public ResponseEntity<ApiResponse<String>> createOutletTest() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Permission granted",
                        "You can create an outlet"
                )
        );
    }
}
