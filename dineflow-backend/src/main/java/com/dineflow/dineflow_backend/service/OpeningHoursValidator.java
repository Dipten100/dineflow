package com.dineflow.dineflow_backend.service;

import com.dineflow.dineflow_backend.dto.outlet.RegularHoursPeriodRequest;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Component
public class OpeningHoursValidator {

    public void validate(
            List<RegularHoursPeriodRequest> periods
    ) {

        if (periods == null || periods.isEmpty()) {
            return;
        }

        for (RegularHoursPeriodRequest period : periods) {

            if (!period.openTime()
                    .isBefore(period.closeTime())) {

                throw new IllegalArgumentException(
                        "Opening time must be before closing time"
                );
            }
        }

        List<RegularHoursPeriodRequest> sorted =
                periods.stream()
                        .sorted(
                                Comparator.comparing(
                                        RegularHoursPeriodRequest::openTime
                                )
                        )
                        .toList();

        for (int i = 1; i < sorted.size(); i++) {

            LocalTime previousClose =
                    sorted.get(i - 1).closeTime();

            LocalTime currentOpen =
                    sorted.get(i).openTime();

            if (!previousClose.isBefore(currentOpen)) {

                throw new IllegalArgumentException(
                        "Opening hours cannot overlap"
                );
            }
        }
    }
}
