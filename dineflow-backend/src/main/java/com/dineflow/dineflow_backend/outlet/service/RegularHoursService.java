package com.dineflow.dineflow_backend.outlet.service;

import com.dineflow.dineflow_backend.outlet.dto.RegularHoursPeriodRequest;
import com.dineflow.dineflow_backend.outlet.dto.RegularHoursRequest;
import com.dineflow.dineflow_backend.outlet.dto.RegularHoursResponse;
import com.dineflow.dineflow_backend.outlet.entity.Outlet;
import com.dineflow.dineflow_backend.outlet.entity.OutletRegularHours;
import com.dineflow.dineflow_backend.outlet.repository.OutletRegularHoursRepository;
import com.dineflow.dineflow_backend.outlet.repository.OutletRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegularHoursService {

    private final OutletRepository outletRepository;

    private final OutletRegularHoursRepository regularHoursRepository;

    private final OpeningHoursValidator validator;


    @Transactional
    public void save(
            Long outletId,
            RegularHoursRequest request
    ) {

        Outlet outlet =
                outletRepository.findById(outletId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Outlet not found"
                                )
                        );

        validator.validate(request.periods());

        regularHoursRepository
                .deleteByOutletIdAndDayOfWeek(
                        outletId,
                        request.dayOfWeek()
                );

        int periodNumber = 1;

        for (
                RegularHoursPeriodRequest period
                : request.periods()
        ) {

            OutletRegularHours hours =
                    OutletRegularHours.builder()
                            .outlet(outlet)
                            .dayOfWeek(
                                    request.dayOfWeek()
                            )
                            .periodNumber(
                                    periodNumber++
                            )
                            .openTime(
                                    period.openTime()
                            )
                            .closeTime(
                                    period.closeTime()
                            )
                            .closed(false)
                            .createdAt(
                                    LocalDateTime.now()
                            )
                            .updatedAt(
                                    LocalDateTime.now()
                            )
                            .build();

            regularHoursRepository.save(hours);
        }
    }


    public OutletRegularHours getRegularHours(Long id) {
        return regularHoursRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Regular hours not found"
                        )
                );
    }

    @Transactional
    public void deleteRegularHours(Long id) {
        regularHoursRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<RegularHoursResponse> getRegularHoursByOutletId(Long outletId) {
        return regularHoursRepository.findByOutletIdOrderByDayOfWeekAscPeriodNumberAsc(outletId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private RegularHoursResponse mapToResponse(OutletRegularHours hours) {
        return new RegularHoursResponse(
                hours.getId(),
                hours.getOutlet().getId(),
                hours.getDayOfWeek().toString(),
                hours.getOpenTime() != null ? hours.getOpenTime().toString() : null,
                hours.getCloseTime() != null ? hours.getCloseTime().toString() : null,
                hours.isClosed()
        );
    }
}
