package com.dineflow.dineflow_backend.entity;

import com.dineflow.dineflow_backend.entity.enums.OutletStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "outlets")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Outlet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "restaurant_id",
            nullable = false
    )
    private Restaurant restaurant;

    @Column(
            nullable = false,
            length = 150
    )
    private String name;

    @Column(
            nullable = false,
            length = 50,
            unique = true
    )
    private String code;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(length = 30)
    private String phone;

    @Column(name = "time_zone", length = 50, nullable = false)
    private String timeZone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutletStatus status;

    @OneToMany(mappedBy = "outlet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OutletRegularHours> regularHours;

    @OneToMany(mappedBy = "outlet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OutletSpecialHours> specialHours;

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

        if (status == null) {
            status = OutletStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
