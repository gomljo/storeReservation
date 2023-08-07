package com.store.reservation.store.store.controller;

import com.store.reservation.member.domain.Member;
import com.store.reservation.member.service.MemberService;
import com.store.reservation.store.store.domain.vo.Location;
import com.store.reservation.store.store.dto.create.CreateStore;
import com.store.reservation.store.store.dto.update.UpdateStore;
import com.store.reservation.store.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final MemberService memberService;
    @PostMapping("/register")
    @PreAuthorize("hasRole('PARTNER')")
    public CreateStore.Response registerStore(
            @RequestBody CreateStore.Request request) {
        Member member = memberService.searchBy(request.getMemberId());
        Location location = storeService.convertCoordinateFrom(request.getRoadName());
        return CreateStore.Response.fromEntity(
                storeService.create(request, location, member));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('PARTNER')")
    public UpdateStore.Response updateStore(
            @RequestBody UpdateStore.Request request) {
        return UpdateStore.Response.fromEntity(storeService.update(request));
    }
}
