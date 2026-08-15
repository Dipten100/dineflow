package com.dineflow.dineflow_backend.service;

import com.dineflow.dineflow_backend.dto.auth.LoginRequest;
import com.dineflow.dineflow_backend.dto.auth.LoginResponse;
import com.dineflow.dineflow_backend.dto.auth.RegisterRequest;
import com.dineflow.dineflow_backend.entity.Customer;
import com.dineflow.dineflow_backend.entity.Permission;
import com.dineflow.dineflow_backend.entity.Role;
import com.dineflow.dineflow_backend.entity.User;
import com.dineflow.dineflow_backend.repository.CustomerRepository;
import com.dineflow.dineflow_backend.repository.RoleRepository;
import com.dineflow.dineflow_backend.repository.UserRepository;
import com.dineflow.dineflow_backend.security.JwtService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final CustomerRepository customerRepository;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;

        @Transactional
        public void registerCustomer(RegisterRequest request) {

                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new IllegalArgumentException(
                                        "Email already registered");
                }

                if (request.getPhone() != null &&
                                userRepository.existsByPhone(request.getPhone())) {

                        throw new IllegalArgumentException(
                                        "Phone already registered");
                }

                Role customerRole = roleRepository
                                .findByName("CUSTOMER")
                                .orElseThrow(() -> new IllegalStateException(
                                                "CUSTOMER role not configured"));

                User user = User.builder()
                                .email(request.getEmail().toLowerCase())
                                .phone(request.getPhone())
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .password(
                                                passwordEncoder.encode(
                                                                request.getPassword()))
                                .status("ACTIVE")
                                .build();

                user.getRoles().add(customerRole);

                userRepository.save(user);

                Customer customer = Customer.builder()
                                .user(user)
                                .loyaltyPoints(0)
                                .build();

                customerRepository.save(customer);
        }

        @Transactional(readOnly = true)
        public LoginResponse login(LoginRequest request) {

                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                User user = userRepository.findByEmail(
                                request.getEmail().toLowerCase()).orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "Invalid email or password"));

                String token = jwtService.generateToken(user);

                List<String> roles = user.getRoles()
                                .stream()
                                .map(Role::getName)
                                .toList();

                List<String> permissions = user.getRoles()
                                .stream()
                                .flatMap(role -> role.getPermissions().stream())
                                .map(Permission::getName)
                                .distinct()
                                .toList();

                return LoginResponse.builder()
                                .userId(user.getId())
                                .email(user.getEmail())
                                .accessToken(token)
                                .tokenType("Bearer")
                                .roles(roles)
                                .permissions(permissions)
                                .superAdmin(user.isSuperAdmin())
                                .build();
        }
}
