package com.dineflow.dineflow_backend.auth.service;

import com.dineflow.dineflow_backend.auth.dto.ForgotPasswordRequest;
import com.dineflow.dineflow_backend.auth.dto.LoginRequest;
import com.dineflow.dineflow_backend.auth.dto.LoginResponse;
import com.dineflow.dineflow_backend.auth.dto.RegisterRequest;
import com.dineflow.dineflow_backend.auth.dto.ResetPasswordRequest;
import com.dineflow.dineflow_backend.customer.entity.Customer;
import com.dineflow.dineflow_backend.auth.entity.PasswordResetToken;
import com.dineflow.dineflow_backend.permission.entity.Permission;
import com.dineflow.dineflow_backend.permission.entity.Role;
import com.dineflow.dineflow_backend.user.entity.User;
import com.dineflow.dineflow_backend.customer.repository.CustomerRepository;
import com.dineflow.dineflow_backend.auth.repository.PasswordResetTokenRepository;
import com.dineflow.dineflow_backend.permission.repository.RoleRepository;
import com.dineflow.dineflow_backend.user.repository.UserRepository;
import com.dineflow.dineflow_backend.security.JwtService;
import com.dineflow.dineflow_backend.security.TokenHashService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
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
        private final PasswordResetTokenRepository passwordResetTokenRepository;
        private final PasswordResetTokenService passwordResetTokenService;
        private final TokenHashService tokenHashService;

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

        @Transactional
        public String forgotPassword(
                        ForgotPasswordRequest request) {

                String email = request.getEmail()
                                .trim()
                                .toLowerCase();

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "No account found with this email"));

                // Remove old reset tokens
                passwordResetTokenRepository
                                .deleteByUserId(user.getId());

                String rawToken = passwordResetTokenService.generateToken();

                String tokenHash = tokenHashService.hash(rawToken);

                PasswordResetToken resetToken = PasswordResetToken.builder()
                                .user(user)
                                .tokenHash(tokenHash)
                                .expiresAt(
                                                LocalDateTime.now()
                                                                .plusMinutes(15))
                                .createdAt(LocalDateTime.now())
                                .build();

                passwordResetTokenRepository.save(
                                resetToken);

                /*
                 * DEVELOPMENT ONLY
                 *
                 * Later this token will be sent by email.
                 */
                return rawToken;
        }

        @Transactional
        public void resetPassword(
                        ResetPasswordRequest request) {

                String tokenHash = tokenHashService.hash(
                                request.getToken());

                PasswordResetToken resetToken = passwordResetTokenRepository
                                .findByTokenHash(tokenHash)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Invalid password reset token"));

                if (resetToken.getUsedAt() != null) {

                        throw new IllegalArgumentException(
                                        "Password reset token has already been used");
                }

                if (resetToken.getExpiresAt()
                                .isBefore(LocalDateTime.now())) {

                        throw new IllegalArgumentException(
                                        "Password reset token has expired");
                }

                User user = resetToken.getUser();

                user.setPassword(
                                passwordEncoder.encode(
                                                request.getNewPassword()));

                userRepository.save(user);

                resetToken.setUsedAt(
                                LocalDateTime.now());

                passwordResetTokenRepository.save(
                                resetToken);
        }

}
