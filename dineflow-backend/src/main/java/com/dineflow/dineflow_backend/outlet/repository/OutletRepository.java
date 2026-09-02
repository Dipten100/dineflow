package com.dineflow.dineflow_backend.outlet.repository;

import com.dineflow.dineflow_backend.outlet.entity.Outlet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OutletRepository
        extends JpaRepository<Outlet, Long> {

    Optional<Outlet> findByCode(String code);

    boolean existsByCode(String code);

    List<Outlet> findByRestaurantId(Long restaurantId);

    boolean existsByRestaurantIdAndName(
            Long restaurantId,
            String name);
}
