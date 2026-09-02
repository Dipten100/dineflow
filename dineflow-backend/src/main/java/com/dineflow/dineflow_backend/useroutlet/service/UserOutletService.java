package com.dineflow.dineflow_backend.useroutlet.service;

import com.dineflow.dineflow_backend.useroutlet.dto.UserOutletResponse;
import com.dineflow.dineflow_backend.outlet.entity.Outlet;
import com.dineflow.dineflow_backend.user.entity.User;
import com.dineflow.dineflow_backend.useroutlet.entity.UserOutlet;
import com.dineflow.dineflow_backend.outlet.repository.OutletRepository;
import com.dineflow.dineflow_backend.useroutlet.repository.UserOutletRepository;
import com.dineflow.dineflow_backend.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserOutletService {

    private final UserRepository userRepository;

    private final OutletRepository outletRepository;

    private final UserOutletRepository userOutletRepository;


    @Transactional
    public UserOutletResponse assign(
            Long userId,
            Long outletId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "User not found"
                                )
                        );

        Outlet outlet =
                outletRepository.findById(outletId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Outlet not found"
                                )
                        );

        if (userOutletRepository
                .existsByUserIdAndOutletId(
                        userId,
                        outletId
                )) {

            throw new IllegalArgumentException(
                    "User is already assigned to this outlet"
            );
        }

        UserOutlet userOutlet =
                UserOutlet.builder()
                        .user(user)
                        .outlet(outlet)
                        .createdAt(
                                LocalDateTime.now()
                        )
                        .build();

        userOutletRepository.save(userOutlet);

        return toResponse(userOutlet);
    }


    @Transactional(readOnly = true)
    public List<UserOutletResponse> findByUser(
            Long userId
    ) {

        if (!userRepository.existsById(userId)) {

            throw new IllegalArgumentException(
                    "User not found"
            );
        }

        return userOutletRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<UserOutletResponse> findByOutlet(
            Long outletId
    ) {

        if (!outletRepository.existsById(outletId)) {

            throw new IllegalArgumentException(
                    "Outlet not found"
            );
        }

        return userOutletRepository
                .findByOutletIdOrderByCreatedAtDesc(outletId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional
    public void remove(
            Long userId,
            Long outletId
    ) {

        if (!userOutletRepository
                .existsByUserIdAndOutletId(
                        userId,
                        outletId
                )) {

            throw new IllegalArgumentException(
                    "User is not assigned to this outlet"
            );
        }

        userOutletRepository
                .deleteByUserIdAndOutletId(
                        userId,
                        outletId
                );
    }


    private UserOutletResponse toResponse(
            UserOutlet userOutlet
    ) {

        return new UserOutletResponse(
                userOutlet.getUser().getId(),
                userOutlet.getOutlet().getId(),
                userOutlet.getOutlet().getName()
        );
    }
}
