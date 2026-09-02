package com.dineflow.dineflow_backend.restaurant.repository;

import com.dineflow.dineflow_backend.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository
        extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.outlets ORDER BY r.id DESC")
    List<Restaurant> findAllWithOutlets();

    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.outlets WHERE r.id = :id")
    Optional<Restaurant> findByIdWithOutlets(Long id);

    @Query("SELECT r FROM Restaurant r ORDER BY r.id DESC")
    List<Restaurant> findAll();

    @Query("SELECT r.id, r.name, r.description, r.email, r.phone, r.status, (SELECT COUNT(o) FROM Outlet o WHERE o.restaurant = r), r.createdAt, r.updatedAt FROM Restaurant r ORDER BY r.id DESC")
    List<Object[]> findAllWithCountRaw();
}
