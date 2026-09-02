package com.dineflow.dineflow_backend.permission.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dineflow.dineflow_backend.permission.entity.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    
}
