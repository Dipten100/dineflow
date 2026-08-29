package com.dineflow.dineflow_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dineflow.dineflow_backend.dto.ApiResponse;
import com.dineflow.dineflow_backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @PostMapping("/user/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User logged out successfully",
                        null
                )
        );
    }
}
