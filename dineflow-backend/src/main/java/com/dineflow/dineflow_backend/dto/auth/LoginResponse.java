package com.dineflow.dineflow_backend.dto.auth;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LoginResponse {

    private Long userId;

    private String email;

    private String accessToken;

    private String tokenType;

    private List<String> roles;

    private List<String> permissions;

    private boolean superAdmin;
}
