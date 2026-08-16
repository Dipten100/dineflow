package com.dineflow.dineflow_backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.dineflow.dineflow_backend.entity.User;
import com.dineflow.dineflow_backend.repository.UserOutletRepository;

import lombok.RequiredArgsConstructor;

@Service("permissionService")
@RequiredArgsConstructor
public class PermissionService {

        private final UserOutletRepository userOutletRepository;

        public boolean hasPermission(
                        Authentication authentication,
                        String permission) {

                if (authentication == null ||
                                !authentication.isAuthenticated()) {

                        return false;
                }

                return authentication
                                .getAuthorities()
                                .stream()
                                .anyMatch(authority -> authority.getAuthority()
                                                .equals(permission));
        }

        private boolean hasRole(
                        User user,
                        String roleName) {

                return user.getRoles()
                                .stream()
                                .anyMatch(
                                                role -> role.getName()
                                                                .equals(roleName));
        }

        private User getUser(Authentication authentication) {
                return (User) authentication.getPrincipal();
        }

        public boolean canAccessOutlet(
                        Authentication authentication,
                        Long outletId) {

                User user = getUser(authentication);

                /*
                 * Super admin can access everything.
                 */
                if (hasRole(user, "SUPER_ADMIN")) {
                        return true;
                }

                /*
                 * Otherwise user must be explicitly
                 * assigned to this outlet.
                 */
                return userOutletRepository
                                .existsByUserIdAndOutletId(
                                                user.getId(),
                                                outletId);
        }
}
