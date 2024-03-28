package com.store.reservation.store.controller;

import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.model.SecurityUser;
import com.store.reservation.member.service.MemberService;
import com.store.reservation.store.constant.StoreStatus;
import com.store.reservation.store.dto.create.CreateStoreDto;
import com.store.reservation.store.dto.search.request.SearchStoreDto;
import com.store.reservation.store.dto.search.response.StoreDto;
import com.store.reservation.store.dto.update.UpdateStoreDto;
import com.store.reservation.store.repository.queryDsl.dto.SimpleStore;
import com.store.reservation.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final MemberService memberService;


    @PostMapping
    @PreAuthorize("hasRole('PARTNER')")
    public ResponseEntity<StoreStatus> registerStore(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestBody CreateStoreDto createStoreDto) {
        MemberInformation memberInformation = memberService.searchBy(securityUser.getId());
        storeService.create(createStoreDto, memberInformation);
        return ResponseEntity.ok(StoreStatus.CREATION_SUCCESS);
    }

    @PutMapping("/{storeId}")
    @PreAuthorize("hasRole('PARTNER')")
    public ResponseEntity<StoreStatus> updateStore(
            @AuthenticationPrincipal SecurityUser securityUser,
            @PathVariable Long storeId,
            @RequestBody UpdateStoreDto storeDto) {
        MemberInformation memberInformation = memberService.searchBy(securityUser.getId());
        storeService.update(storeDto, storeId, memberInformation);
        return ResponseEntity.ok(StoreStatus.UPDATE_SUCCESS);
    }

    @GetMapping("customer/{pageIndex}/{pageSize}")
    public Page<SimpleStore> searchStoreBy(@RequestParam int pageIndex,
                                           @RequestParam int pageSize,
                                           @RequestBody SearchStoreDto searchStoreDto) {
        return storeService.searchStoreListBy(searchStoreDto, PageRequest.of(pageSize, pageIndex));
    }

    @GetMapping("manager/detail/{storeId}")
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    public ResponseEntity<StoreDto> searchStoreByManager(@AuthenticationPrincipal SecurityUser securityUser,
                                                @PathVariable Long storeId) {
        MemberInformation memberInformation = memberService.searchBy(securityUser.getId());
        return ResponseEntity.ok(StoreDto.from(storeService.searchStoreByOwner(memberInformation, storeId)));
    }

    @GetMapping("customer/detail/{storeId}")
    public ResponseEntity<StoreDto> searchStoreByCustomer(@PathVariable Long storeId){
        return ResponseEntity.ok(StoreDto.from(storeService.searchStoreByCustomer(storeId)));
    }
}
