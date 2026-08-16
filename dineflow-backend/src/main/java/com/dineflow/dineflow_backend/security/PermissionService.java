package com.dineflow.dineflow_backend.security;

import org.springframework.security.core.Authentication;
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

        return authentication
                .getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority()
                                .equals(permission)
                );
    }
}
