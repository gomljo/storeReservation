package com.store.reservation.store.service;

import com.store.reservation.member.memberInfo.domain.MemberInformation;
import com.store.reservation.member.memberInfo.exception.MemberError;
import com.store.reservation.member.memberInfo.exception.MemberException;
import com.store.reservation.store.domain.model.Store;
import com.store.reservation.store.domain.vo.location.Location;
import com.store.reservation.store.domain.vo.operating.OperatingHours;
import com.store.reservation.store.dto.create.StoreDto;
import com.store.reservation.store.exception.StoreErrorCode;
import com.store.reservation.store.exception.StoreException;
import com.store.reservation.store.repository.command.StoreRepository;
import com.store.reservation.store.repository.query.StoreSearchRepository;
import com.store.reservation.store.util.implementation.kakao.dto.LocationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.store.reservation.store.exception.StoreErrorCode.ALREADY_REGISTERED_STORE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreSearchRepository storeSearchRepository;

    public Store create(StoreDto storeDto,
                        LocationDto locationDto,
                        MemberInformation memberInformation) {
        if (storeSearchRepository.existsStoreByRoadNameAndStoreName(
                storeDto.getRoadName(),
                storeDto.getStoreName())) {
            throw new StoreException(ALREADY_REGISTERED_STORE_NAME);
        }
        Location location = Location.createBy(locationDto);

        OperatingHours operatingHours = OperatingHours.createBy(storeDto);
        operatingHours.defineReservationTimes();

        return storeRepository.save(Store.builder()
                .storeName(storeDto.getStoreName())
                .memberInformation(memberInformation)
                .location(location)
                .operatingHours(operatingHours)
                .foods(storeDto.getFoodListToSet())
                .build());
    }

    public void update(StoreDto storeDto,
                       LocationDto locationDto,
                       MemberInformation memberInformation) {
        if (!storeRepository.existsStoreByStoreNameAndMemberInformation(
                storeDto.getStoreName(),
                memberInformation
        )) {
            throw new MemberException(MemberError.ACCESS_DENIED);
        }
        Store store = storeRepository.findByStoreName(storeDto.getStoreName())
                .orElseThrow(() -> new StoreException(StoreErrorCode.UPDATE_BEFORE_CREATE_STORE));
        store.update(storeDto, locationDto);
        storeRepository.save(store);
    }

    public Store search(long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.NO_SUCH_STORE));
    }

}
