package com.dineflow.dineflow_backend.repository;

import com.dineflow.dineflow_backend.entity.Staff;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByUserId(Long userId);

    Optional<Staff> findByEmployeeCode(String employeeCode);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByUserId(Long userId);
}
