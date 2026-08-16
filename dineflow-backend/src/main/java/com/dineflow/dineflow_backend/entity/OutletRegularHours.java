package com.dineflow.dineflow_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
        name = "outlet_regular_hours",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_outlet_regular_hours_period",
                        columnNames = {
                                "outlet_id",
                                "day_of_week",
                                "period_number"
                        }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutletRegularHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "outlet_id",
            nullable = false
    )
    private Outlet outlet;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "day_of_week",
            nullable = false,
            length = 20
    )
    private DayOfWeek dayOfWeek;

    /**
     * Allows multiple opening periods in one day.
     *
     * Example:
     * Monday
     * 1 -> 09:00 - 14:00
     * 2 -> 17:00 - 23:00
     */
    @Column(
            name = "period_number",
            nullable = false
    )
    private Integer periodNumber;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    /**
     * true = outlet closed for this day
     */
    @Column(
            name = "is_closed",
            nullable = false
    )
    @Builder.Default
    private boolean closed = false;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}
