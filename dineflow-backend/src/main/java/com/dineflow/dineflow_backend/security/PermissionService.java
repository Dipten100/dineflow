package com.dineflow.dineflow_backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service("permissionService")
public class PermissionService {

    public boolean hasPermission(
            Authentication authentication,
            String permission
    ) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return false;
        }

        // SUPER_ADMIN can do everything
        boolean isSuperAdmin = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_SUPER_ADMIN"::equals);

        if (isSuperAdmin) {
            return true;
        }

        // Normal permission check
        return authentication
                .getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority()
                                .equals(permission)
                );
    }
}
