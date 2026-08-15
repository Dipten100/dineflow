package com.dineflow.dineflow_backend.controller;

import com.dineflow.dineflow_backend.dto.ApiResponse;
import com.dineflow.dineflow_backend.dto.auth.ForgotPasswordRequest;
import com.dineflow.dineflow_backend.dto.auth.LoginRequest;
import com.dineflow.dineflow_backend.dto.auth.LoginResponse;
import com.dineflow.dineflow_backend.dto.auth.RegisterRequest;
import com.dineflow.dineflow_backend.dto.auth.ResetPasswordRequest;
import com.dineflow.dineflow_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

        private final AuthService authService;

        @PostMapping("/register")
        public ResponseEntity<ApiResponse<Void>> register(
                        @Valid @RequestBody RegisterRequest request) {

                authService.registerCustomer(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(
                                                ApiResponse.success(
                                                                "Customer registered successfully"));
        }

        @PostMapping("/login")
        public ResponseEntity<ApiResponse<LoginResponse>> login(
                        @Valid @RequestBody LoginRequest request) {

                LoginResponse response = authService.login(request);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Login successful",
                                                response));
        }

        @PostMapping("/forgot-password")
        public ResponseEntity<ApiResponse<String>> forgotPassword(
                        @Valid @RequestBody ForgotPasswordRequest request) {

                String token = authService.forgotPassword(request);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Password reset token generated",
                                                token));
        }

        @PostMapping("/reset-password")
        public ResponseEntity<ApiResponse<Void>> resetPassword(
                        @Valid @RequestBody ResetPasswordRequest request) {

                authService.resetPassword(request);

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Password reset successfully",
                                                null));
        }
}
