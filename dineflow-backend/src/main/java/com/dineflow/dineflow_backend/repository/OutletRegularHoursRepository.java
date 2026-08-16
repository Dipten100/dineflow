package com.dineflow.dineflow_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dineflow.dineflow_backend.entity.OutletRegularHours;

import java.time.DayOfWeek;
import java.util.List;

public interface OutletRegularHoursRepository
        extends JpaRepository<OutletRegularHours, Long> {

    List<OutletRegularHours> findByOutletIdOrderByDayOfWeekAscPeriodNumberAsc(
            Long outletId
    );

    void deleteByOutletIdAndDayOfWeek(
            Long outletId,
            DayOfWeek dayOfWeek
    );
}
