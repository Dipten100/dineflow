package com.dineflow.dineflow_backend.useroutlet.repository;

import com.dineflow.dineflow_backend.useroutlet.entity.UserOutlet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserOutletRepository
        extends JpaRepository<UserOutlet, Long> {

    boolean existsByUserIdAndOutletId(
            Long userId,
            Long outletId
    );

    List<UserOutlet> findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    List<UserOutlet> findByOutletIdOrderByCreatedAtDesc(
            Long outletId
    );

    void deleteByUserIdAndOutletId(
            Long userId,
            Long outletId
    );
}