package com.dineflow.dineflow_backend.service;

import com.dineflow.dineflow_backend.dto.outlet.SpecialHoursRequest;
import com.dineflow.dineflow_backend.dto.outlet.SpecialHoursResponse;
import com.dineflow.dineflow_backend.entity.Outlet;
import com.dineflow.dineflow_backend.entity.OutletSpecialHours;
import com.dineflow.dineflow_backend.repository.OutletRepository;
import com.dineflow.dineflow_backend.repository.OutletSpecialHoursRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialHoursService {

    private final OutletRepository outletRepository;
    private final OutletSpecialHoursRepository specialHoursRepository;

    @Transactional
    public void save(
            Long outletId,
            SpecialHoursRequest request
    ) {

        Outlet outlet =
                outletRepository.findById(outletId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Outlet not found"
                                )
                        );

        specialHoursRepository
                .deleteByOutletIdAndDate(
                        outletId,
                        request.date()
                );

        OutletSpecialHours hours =
                OutletSpecialHours.builder()
                        .outlet(outlet)
                        .date(request.date())
                        .openTime(request.openTime())
                        .closeTime(request.closeTime())
                        .closed(request.isClosed())
                        .reason(request.reason())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

        specialHoursRepository.save(hours);
    }

    @Transactional(readOnly = true)
    public OutletSpecialHours getSpecialHours(Long id) {
        return specialHoursRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Special hours not found"
                        )
                );
    }

    @Transactional
    public void deleteSpecialHours(Long id) {
        specialHoursRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<SpecialHoursResponse> getSpecialHoursByOutletId(Long outletId) {
        return specialHoursRepository.findByOutletIdOrderByDateAsc(outletId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private SpecialHoursResponse mapToResponse(OutletSpecialHours hours) {
        return new SpecialHoursResponse(
                hours.getId(),
                hours.getOutlet().getId(),
                hours.getDate() != null ? hours.getDate().toString() : null,
                hours.getOpenTime() != null ? hours.getOpenTime().toString() : null,
                hours.getCloseTime() != null ? hours.getCloseTime().toString() : null,
                hours.getReason(),
                hours.isClosed()
        );
    }
}
