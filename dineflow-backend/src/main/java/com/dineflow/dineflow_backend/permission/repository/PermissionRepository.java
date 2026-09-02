package com.dineflow.dineflow_backend.permission.repository;

import com.dineflow.dineflow_backend.permission.entity.Permission;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query("SELECT p FROM Permission p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.module) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.action) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Permission> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(DISTINCT p.module) FROM Permission p")
    long countDistinctModules();

    @Query("SELECT COUNT(DISTINCT p.action) FROM Permission p")
    long countDistinctActions();

    Optional<Permission> findByName(String name);

    boolean existsByName(String name);
}
