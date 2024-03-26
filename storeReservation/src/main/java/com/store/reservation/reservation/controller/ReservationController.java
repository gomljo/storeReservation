package com.store.reservation.reservation.reservation.controller;

import com.store.reservation.member.memberInfo.model.SecurityUser;
import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.reservation.domain.model.type.ReservationState;
import com.store.reservation.reservation.reservation.dto.ReservationStatusDto;
import com.store.reservation.reservation.reservation.dto.SearchReservation;
import com.store.reservation.reservation.reservation.dto.UpdateApproval;
import com.store.reservation.reservation.reservation.service.ReservationModuleService;
import com.store.reservation.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationModuleService reservationModuleService;
    private final StoreService storeService;
    @PutMapping("/update/approval")
    @PreAuthorize("hasRole('PARTNER')")
    public UpdateApproval.Response updateApprovalState(
            @RequestParam long reservationId,
            @RequestBody UpdateApproval.Request request) {

        return UpdateApproval.Response.fromEntity(
                reservationModuleService.updateApprovalState(reservationId, request.getApprovalState()));
    }


//    @GetMapping("/reserve/today-status")
//    public SearchReservation.Response searchAllReservationToday(@RequestBody ReservationStatusDto reservationStatusDto) {
//
//        Page<Reservation> reservationPage = reservationModuleService.searchByCurrentDate(reservationStatusDto);
//
//
//        return SearchReservation.Response.from(
//                reservationModuleService.searchByCurrentDate(reservationStatusDto));
//    }

//    @GetMapping("/reserve/reservation/{reservationStatus}")
//    @PreAuthorize("hasRole('PARTNER')")
//    public SearchReservation.Response searchByStatus(@AuthenticationPrincipal SecurityUser securityUser,
//                                                     @PathVariable ReservationState reservationStatus,
//                                                     @RequestBody ReservationStatusDto reservationStatusDto){
//
//        return SearchReservation.Response.from(reservationModuleService.searchByStatus(
//                reservationStatus, reservationStatusDto, securityUser.getId()));
//    }
}
