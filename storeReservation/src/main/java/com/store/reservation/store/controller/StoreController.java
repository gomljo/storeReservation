package com.store.reservation.store.controller;

import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.security.userDetails.SecurityUser;
import com.store.reservation.member.service.MemberService;
import com.store.reservation.store.constant.SearchCondition;
import com.store.reservation.store.constant.StoreStatus;
import com.store.reservation.store.dto.common.StoreDto;
import com.store.reservation.store.dto.search.request.SearchStoreDto;
import com.store.reservation.store.dto.search.response.StoreDetailForCustomerDto;
import com.store.reservation.store.dto.search.response.StoreDetailForManagerDto;
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
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    public ResponseEntity<StoreStatus> registerStore(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestBody StoreDto storeDto) {
        MemberInformation memberInformation = memberService.searchBy(securityUser.getId());
        storeService.create(storeDto, memberInformation);
        return ResponseEntity.ok(StoreStatus.CREATION_SUCCESS);
    }

    @PutMapping("/{storeId}")
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    public ResponseEntity<StoreStatus> updateStore(
            @AuthenticationPrincipal SecurityUser securityUser,
            @PathVariable Long storeId,
            @RequestBody StoreDto storeDto) {
        MemberInformation memberInformation = memberService.searchBy(securityUser.getId());
        storeService.update(storeDto, storeId, memberInformation);
        return ResponseEntity.ok(StoreStatus.UPDATE_SUCCESS);
    }

    @GetMapping("/customer/{pageIndex}/{pageSize}/{searchCondition}")
    public Page<StoreDetailForCustomerDto> searchStoreBy(@PathVariable int pageIndex,
                                                         @PathVariable int pageSize,
                                                         @PathVariable SearchCondition searchCondition,
                                                         @RequestBody SearchStoreDto searchStoreDto) {
        return storeService.searchStoreListBy(searchStoreDto, searchCondition, PageRequest.of(pageSize, pageIndex));
    }

    @GetMapping("/manager/detail/{storeId}")
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    public ResponseEntity<StoreDetailForManagerDto> searchStoreByManager(@AuthenticationPrincipal SecurityUser securityUser,
                                                                          @PathVariable Long storeId) {
        MemberInformation memberInformation = memberService.searchBy(securityUser.getId());
        return ResponseEntity.ok(StoreDetailForManagerDto.from(storeService.searchStoreByOwner(memberInformation, storeId)));
    }

    @GetMapping("/customer/detail/{storeId}")
    public ResponseEntity<StoreDetailForCustomerDto> searchStoreByCustomer(@PathVariable Long storeId){
        return ResponseEntity.ok(StoreDetailForCustomerDto.from(storeService.searchStoreByCustomer(storeId)));
    }
}
