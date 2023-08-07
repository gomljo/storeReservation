package com.store.reservation.global.review.controller;

import com.store.reservation.global.review.dto.CreateReview;
import com.store.reservation.global.review.dto.SearchLog;
import com.store.reservation.global.review.service.ReviewComponentService;
import com.store.reservation.reservation.log.service.ReservationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewComponentController {

    private final ReviewComponentService reviewComponentService;
    private final ReservationLogService reservationLogService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public CreateReview.Response createReview(
            @RequestParam long logId,
            @RequestBody CreateReview.Request request) {
        return CreateReview.Response.fromEntity(
                reviewComponentService.createReview(logId, request));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public SearchLog.Response searchLog(@RequestParam long customerId) {
        return SearchLog.Response.fromEntity(
                reservationLogService.searchAllReservationLogBy(customerId));
    }

}
