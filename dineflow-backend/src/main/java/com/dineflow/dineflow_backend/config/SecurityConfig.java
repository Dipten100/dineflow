package com.dineflow.dineflow_backend.config;

import com.dineflow.dineflow_backend.security.CustomJwtAuthenticationConverter;
import com.dineflow.dineflow_backend.security.CustomUserDetailsService;
import com.dineflow.dineflow_backend.security.RestAccessDeniedHandler;
import com.dineflow.dineflow_backend.security.RestAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final CustomUserDetailsService userDetailsService;

        private final RestAuthenticationEntryPoint authenticationEntryPoint;

        private final RestAccessDeniedHandler accessDeniedHandler;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories
                                .createDelegatingPasswordEncoder();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {

                DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);

                provider.setPasswordEncoder(passwordEncoder());

                return provider;
        }

        @Bean
        public AuthenticationManager authenticationManager(
                        AuthenticationProvider authenticationProvider) {

                return new org.springframework.security.authentication.ProviderManager(
                                authenticationProvider);
        }

        @Bean
        public CustomJwtAuthenticationConverter jwtAuthenticationConverter() {

                return new CustomJwtAuthenticationConverter();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http,
                        AuthenticationProvider authenticationProvider,
                        CustomJwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())

                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.STATELESS))

                                .authenticationProvider(authenticationProvider)

                                // Authentication / Authorization errors
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(
                                                                authenticationEntryPoint)
                                                .accessDeniedHandler(
                                                                accessDeniedHandler))

                                .authorizeHttpRequests(auth -> auth

                                                // Public APIs
                                                .requestMatchers(
                                                                "/api/auth/**")
                                                .permitAll()

                                                // Everything else requires login
                                                .anyRequest()
                                                .authenticated())

                                // JWT authentication
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .authenticationEntryPoint(
                                                                authenticationEntryPoint)
                                                .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                                                jwtAuthenticationConverter)));

                return http.build();
        }
}
