package com.store.reservation.notification.controller;

import com.store.reservation.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public void send(){

    }
    @GetMapping("/customer/{customerId}")
    public void searchForCustomerBy(@PathVariable Long customerId){

    }

    @GetMapping("/store/{storeId}")
    public void searchForStoreBy(@PathVariable Long storeId){

    }

}
