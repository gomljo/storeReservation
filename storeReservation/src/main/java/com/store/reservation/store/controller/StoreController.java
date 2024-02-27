package com.store.reservation.store.controller;

import com.store.reservation.member.memberInfo.domain.MemberInformation;
import com.store.reservation.member.memberInfo.model.SecurityUser;
import com.store.reservation.member.memberInfo.service.MemberService;
import com.store.reservation.store.dto.create.StoreDto;
import com.store.reservation.store.service.StoreService;
import com.store.reservation.store.util.GeoCoding;
import com.store.reservation.store.util.implementation.kakao.dto.LocationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final MemberService memberService;
    private final GeoCoding geoCoding;

    @PostMapping("/register")
    @PreAuthorize("hasRole('PARTNER')")
    public void registerStore(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestBody StoreDto storeDto) {

        MemberInformation memberInformation = memberService.searchBy(securityUser.getId());
        LocationDto locationDto = geoCoding.convertToCoordinateFrom(storeDto.getRoadName());
        storeService.create(storeDto, locationDto, memberInformation);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('PARTNER')")
    public void updateStore(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestBody StoreDto storeDto) {
        MemberInformation memberInformation = memberService.searchBy(securityUser.getId());
        LocationDto locationDto = geoCoding.convertToCoordinateFrom(storeDto.getRoadName());
        storeService.update(storeDto, locationDto, memberInformation);
    }
}
