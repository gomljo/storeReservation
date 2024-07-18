package com.store.reservation.global.reservation.controller;

import com.store.reservation.common.dto.SearchResponse;
import com.store.reservation.global.reservation.facade.ReservationFacadeService;
import com.store.reservation.global.reservation.state.ReservationStatusCode;
import com.store.reservation.member.security.userDetails.SecurityUser;
import com.store.reservation.reservation.constants.search.Duration;
import com.store.reservation.reservation.constants.search.SortOption;
import com.store.reservation.reservation.constants.state.ArrivalState;
import com.store.reservation.reservation.constants.state.ReservationState;
import com.store.reservation.reservation.dto.ReservationDetailForCustomerDto;
import com.store.reservation.reservation.dto.ReservationDto;
import com.store.reservation.reservation.dto.ReservationSearchDto;
import com.store.reservation.reservation.dto.common.ReservationTimeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.store.reservation.global.reservation.state.ReservationStatusCode.RESERVATION_STATUS_CHANGE_SUCCESS;
import static com.store.reservation.global.reservation.state.ReservationStatusCode.RESERVE_SUCCESS;

@Tag(name = "reservation-api", description = "예약 API")
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
    private final ReservationFacadeService reservationFacadeService;


    @Operation(
            summary = "매장 예약 하기",
            description = "예약 정보(예약 날짜, 예약 시간, 예약 상태, 도착 상태, 인원 수)를 통해 매장 예약",
            tags = "reservation-api"
    )
    @PostMapping("/customer/store/{storeId}")
    public ResponseEntity<ReservationStatusCode> reserve(@AuthenticationPrincipal SecurityUser customer,
                                                         @Parameter(name = "storeId", description = "매장의 id", in = ParameterIn.PATH)
                                                         @PathVariable Long storeId,
                                                         @RequestBody ReservationDto reservationDto) {
        reservationFacadeService.reserve(storeId, customer.getId(), reservationDto);
        return ResponseEntity.ok(RESERVE_SUCCESS);
    }

    @Operation(
            summary = "예약 상태 수정하기",
            description = "변경될 예약 상태 값으로 예약 상태 변경",
            tags = "reservation-api"
    )
    @PutMapping("/manager/{reservationId}/store/{storeId}/reservationState")
    public ResponseEntity<ReservationStatusCode> updateReservationState(@AuthenticationPrincipal SecurityUser manager,
                                                                        @Parameter(name = "reservationId", description = "예약 id", in = ParameterIn.PATH)
                                                                        @PathVariable Long reservationId,
                                                                        @Parameter(name = "storeId", description = "매장 id", in = ParameterIn.PATH)
                                                                        @PathVariable Long storeId,
                                                                        @Parameter(name = "reservationState", description = "예약 상태", in = ParameterIn.QUERY)
                                                                        @RequestParam ReservationState reservationState) {

        reservationFacadeService.updateReservationState(manager.getId(), reservationId, storeId, reservationState);
        return ResponseEntity.ok(RESERVATION_STATUS_CHANGE_SUCCESS);
    }

    @Operation(
            summary = "도착 상태 변경하기",
            description = "방문한 고객의 도착 상태 변경하기",
            tags = "reservation-api"
    )
    @PutMapping("/manager/{reservationId}/store/{storeId}/arrivalState")
    public ResponseEntity<ReservationStatusCode> updateArrivalState(@AuthenticationPrincipal SecurityUser manager,
                                                                    @Parameter(name = "reservationId", description = "예약 id", in = ParameterIn.PATH)
                                                                    @PathVariable Long reservationId,
                                                                    @Parameter(name = "storeId", description = "매장 id", in = ParameterIn.PATH)
                                                                    @PathVariable Long storeId,
                                                                    @Parameter(name = "arrivalState", description = "도착 상태", in = ParameterIn.QUERY)
                                                                    @RequestParam ArrivalState arrivalState) {
        reservationFacadeService.updateArrivalState(manager.getId(), reservationId, storeId, arrivalState);
        return ResponseEntity.ok(RESERVATION_STATUS_CHANGE_SUCCESS);
    }

    @Operation(
            summary = "검색 조건에 따른 고객의 예약 목록 조회",
            description = "검색 조건에 따라 고객이 이전에 예약한 예약 기록을 조회",
            tags = "reservation-api"
    )
    @GetMapping("/customer/{customerId}/{pageIndex}/{pageSize}")
    public ResponseEntity<SearchResponse<ReservationDetailForCustomerDto>> searchReservationListForCustomer(@AuthenticationPrincipal SecurityUser customer,
                                                                                                            @Parameter(name = "customerId", description = "고객 id", in = ParameterIn.PATH)
                                                                                                            @PathVariable Long customerId,
                                                                                                            @Parameter(name = "duration", description = "조회하고자 하는 기간", in = ParameterIn.QUERY)
                                                                                                            @RequestParam Duration duration,
                                                                                                            @Parameter(name = "sortOption", description = "정렬 순서", in = ParameterIn.QUERY)
                                                                                                            @RequestParam SortOption sortOption,
                                                                                                            @Parameter(name = "reservationState", description = "조회하고자 하는 예약 상태", in = ParameterIn.QUERY)
                                                                                                            @RequestParam ReservationState reservationState,
                                                                                                            @Parameter(name = "pageSize", description = "한번에 조회할 목록의 크기", in = ParameterIn.PATH)
                                                                                                            @PathVariable int pageSize,
                                                                                                            @Parameter(name = "pageIndex", description = "한번에 조회할 목록의 페이지 번호", in = ParameterIn.PATH)
                                                                                                            @PathVariable int pageIndex) {
        return ResponseEntity.ok(reservationFacadeService.searchReservationListForCustomer(customer, customerId,
                ReservationSearchDto.builder()
                        .duration(duration)
                        .reservationState(reservationState)
                        .sortOption(sortOption)
                        .pageable(PageRequest.of(pageIndex, pageSize))
                        .build()));
    }

    @Operation(
            summary = "검색 조건에 따른 매장의 예약 목록 조회",
            description = "검색 조건에 따라 매장을 예약한 예약 기록을 조회",
            tags = "reservation-api"
    )
    @GetMapping("/manager/store/{storeId}/{pageIndex}/{pageSize}")
    public ResponseEntity<SearchResponse<ReservationDto>> searchReservationListForManager(@AuthenticationPrincipal SecurityUser manager,
                                                                                          @Parameter(name = "storeId", description = "매장 id", in = ParameterIn.PATH)
                                                                                          @PathVariable Long storeId,
                                                                                          @Parameter(name = "duration", description = "조회하고자 하는 기간", in = ParameterIn.QUERY)
                                                                                          @RequestParam Duration duration,
                                                                                          @Parameter(name = "sortOption", description = "정렬 순서", in = ParameterIn.QUERY)
                                                                                          @RequestParam SortOption sortOption,
                                                                                          @Parameter(name = "reservationState", description = "조회하고자 하는 예약 상태", in = ParameterIn.QUERY)
                                                                                          @RequestParam ReservationState reservationState,
                                                                                          @Parameter(name = "pageSize", description = "한번에 조회할 목록의 크기", in = ParameterIn.PATH)
                                                                                          @PathVariable int pageSize,
                                                                                          @Parameter(name = "pageIndex", description = "한번에 조회할 목록의 페이지 번호", in = ParameterIn.PATH)
                                                                                          @PathVariable int pageIndex) {
        return ResponseEntity.ok(reservationFacadeService.searchReservationListForManager(manager.getId(), storeId,
                ReservationSearchDto.builder()
                        .duration(duration)
                        .reservationState(reservationState)
                        .sortOption(sortOption)
                        .pageable(PageRequest.of(pageIndex, pageSize))
                        .build()));
    }

    @Operation(
            summary = "매장의 특정 날짜와 예약 시간대 별 예약 목록 조회",
            description = "매장의 특정 날짜와 예약 시간대 별 예약 목록 조회",
            tags = "reservation-api"
    )
    @GetMapping("/manager/store/{storeId}/specification/{pageIndex}/{pageSize}")
    public ResponseEntity<SearchResponse<ReservationDto>> searchReservationListByDateAndTimeForManager(@AuthenticationPrincipal SecurityUser manager,
                                                                                                       @Parameter(name = "storeId", description = "매장 id", in = ParameterIn.PATH)
                                                                                                       @PathVariable Long storeId,
                                                                                                       @Parameter(name = "pageSize", description = "한번에 조회할 목록의 크기", in = ParameterIn.PATH)
                                                                                                       @PathVariable int pageSize,
                                                                                                       @Parameter(name = "pageIndex", description = "한번에 조회할 목록의 페이지 번호", in = ParameterIn.PATH)
                                                                                                       @PathVariable int pageIndex,
                                                                                                       @Parameter(name = "date", description = "조회를 원하는 날짜", in = ParameterIn.QUERY)
                                                                                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                                                                       @Parameter(name = "time", description = "조회를 원하는 예약 시간", in = ParameterIn.QUERY)
                                                                                                       @RequestParam @DateTimeFormat(pattern = "kk:mm") LocalTime time) {
        return ResponseEntity.ok(reservationFacadeService.searchReservationListByDateAndTimeForManager(manager.getId(), storeId,
                ReservationTimeDto.builder()
                        .reservationDate(date)
                        .reservationTime(time)
                        .build(),
                PageRequest.of(pageIndex, pageSize)));
    }
}
