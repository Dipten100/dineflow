package com.dineflow.dineflow_backend.service;

import com.dineflow.dineflow_backend.dto.outlet.RegularHoursPeriodRequest;
import com.dineflow.dineflow_backend.dto.outlet.RegularHoursRequest;
import com.dineflow.dineflow_backend.dto.outlet.RegularHoursResponse;
import com.dineflow.dineflow_backend.entity.Outlet;
import com.dineflow.dineflow_backend.entity.OutletRegularHours;
import com.dineflow.dineflow_backend.repository.OutletRegularHoursRepository;
import com.dineflow.dineflow_backend.repository.OutletRepository;

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
