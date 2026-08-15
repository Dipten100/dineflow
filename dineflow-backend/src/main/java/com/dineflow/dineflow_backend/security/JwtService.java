package com.dineflow.dineflow_backend.security;

import com.dineflow.dineflow_backend.entity.Permission;
import com.dineflow.dineflow_backend.entity.Role;
import com.dineflow.dineflow_backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;

    private static final long EXPIRATION_SECONDS = 60 * 60;

    public String generateToken(User user) {

        Instant now = Instant.now();

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        List<String> permissions = user.getRoles()
                .stream()
                .flatMap(role ->
                        role.getPermissions().stream()
                )
                .map(Permission::getName)
                .distinct()
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("dineflow")
                .issuedAt(now)
                .expiresAt(
                        now.plusSeconds(EXPIRATION_SECONDS)
                )
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roles", roles)
                .claim("permissions", permissions)
                .claim("superAdmin", user.isSuperAdmin())
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(claims)
        ).getTokenValue();
    }
}
