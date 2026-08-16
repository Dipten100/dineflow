package com.dineflow.dineflow_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dineflow.dineflow_backend.entity.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    
}
