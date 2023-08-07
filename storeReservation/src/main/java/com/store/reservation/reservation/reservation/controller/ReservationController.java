package com.store.reservation.reservation.reservation.controller;

import com.store.reservation.reservation.reservation.dto.SearchReservation;
import com.store.reservation.reservation.reservation.dto.UpdateApproval;
import com.store.reservation.reservation.reservation.service.ReservationModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationModuleService reservationModuleService;

    @PutMapping("/update/approval")
    @PreAuthorize("hasRole('PARTNER')")
    public UpdateApproval.Response updateApprovalState(
            @RequestParam long reservationId,
            @RequestBody UpdateApproval.Request request) {

        return UpdateApproval.Response.fromEntity(
                reservationModuleService.updateApprovalState(reservationId, request.getApprovalState()));
    }


    @GetMapping("/reserve/store")
    @PreAuthorize("hasRole('PARTNER')")
    public SearchReservation.Response searchAllReservationToday(@RequestParam long managerId) {
        return SearchReservation.Response.from(
                reservationModuleService.searchByCurrentDate(managerId));
    }
}
