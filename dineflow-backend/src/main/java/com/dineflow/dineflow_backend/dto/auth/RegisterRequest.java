package com.dineflow.dineflow_backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank
    @Email
    private String email;

    private String phone;

    @NotBlank
    @Size(min = 2, max = 100)
    private String firstName;

    private String lastName;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
}
