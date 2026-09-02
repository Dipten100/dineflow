package com.dineflow.dineflow_backend.outlet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
        name = "outlet_special_hours",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_outlet_special_hours_date",
                        columnNames = {
                                "outlet_id",
                                "special_date"
                        }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutletSpecialHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "outlet_id",
            nullable = false
    )
    private Outlet outlet;

    @Column(
            name = "special_date",
            nullable = false
    )
    private LocalDate date;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(
            name = "is_closed",
            nullable = false
    )
    @Builder.Default
    private boolean closed = false;

    @Column(length = 255)
    private String reason;

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
