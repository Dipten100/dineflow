package com.dineflow.dineflow_backend.repository;

import com.dineflow.dineflow_backend.entity.Customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
