package com.store.reservation.global.reservation.controller;

import com.store.reservation.global.reservation.dto.CreateReservation;
import com.store.reservation.global.reservation.dto.SearchByCondition;
import com.store.reservation.global.reservation.dto.UpdateArrival;
import com.store.reservation.global.reservation.service.ReservationComponentService;
import com.store.reservation.reservation.reservation.dto.UpdateApproval;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationComponentController {

    private final ReservationComponentService reservationComponentService;

    @PostMapping("/reserve/create")
    @PreAuthorize("hasRole('USER')")
    public CreateReservation.Response reserve(@RequestBody CreateReservation.Request request){

        return CreateReservation.Response.fromEntity(reservationComponentService.reserve(request));
    }

    @PutMapping("/update/arrival")
    @PreAuthorize("hasRole('PARTNER')")
    public UpdateArrival.Response updateArrivalState(
            @RequestParam long reservationId,
            @RequestBody UpdateArrival.Request request
    ){
        return UpdateArrival.Response.fromEntity(
                reservationComponentService.updateArrivalState(reservationId, request));
    }

    @GetMapping("/reserve/search/distance")
    @PreAuthorize("hasRole('USER')")
    public SearchByCondition.Response searchByDistance(Pageable pageable,
                                                       @RequestBody SearchByCondition.Request request){
        return SearchByCondition.Response.from(
                reservationComponentService.searchStoreByDistance(pageable, request));
    }

    @GetMapping("/reserve/search/alphabetic")
    @PreAuthorize("hasRole('USER')")
    public SearchByCondition.Response searchByAlphabetic(Pageable pageable,
                                                         @RequestBody SearchByCondition.Request request){
        return SearchByCondition.Response.from(
                reservationComponentService.searchStoreByAlphabetic(pageable, request));
    }

    @GetMapping("/reserve/search/star")
    @PreAuthorize("hasRole('USER')")
    public SearchByCondition.Response searchByStarRating(Pageable pageable,
                                                         @RequestBody SearchByCondition.Request request){
        return SearchByCondition.Response.from(
                reservationComponentService.searchStoreByStarRating(pageable, request));
    }

}
