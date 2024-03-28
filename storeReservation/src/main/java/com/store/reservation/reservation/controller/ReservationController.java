package com.store.reservation.reservation.controller;

import com.store.reservation.reservation.reservation.service.ReservationModuleService;
import com.store.reservation.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationModuleService reservationModuleService;
    private final StoreService storeService;

}
