package com.dineflow.dineflow_backend.outlet.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dineflow.dineflow_backend.outlet.entity.OutletSpecialHours;

public interface OutletSpecialHoursRepository extends JpaRepository<OutletSpecialHours, Long> {
    List<OutletSpecialHours> findByOutletIdOrderByDateAsc(Long outletId);

    void deleteByOutletIdAndDate(Long outletId, LocalDate date);
}
