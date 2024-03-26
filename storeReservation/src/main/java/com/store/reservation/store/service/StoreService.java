package com.store.reservation.store.service;

import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.store.domain.model.Store;
import com.store.reservation.store.domain.vo.location.Location;
import com.store.reservation.store.domain.vo.operating.OperatingHours;
import com.store.reservation.store.dto.create.CreateStoreDto;
import com.store.reservation.store.dto.search.request.SearchStoreDto;
import com.store.reservation.store.dto.update.UpdateStoreDto;
import com.store.reservation.store.exception.StoreErrorCode;
import com.store.reservation.store.exception.StoreException;
import com.store.reservation.store.repository.jpa.StoreRepository;
import com.store.reservation.store.repository.queryDsl.StoreSearchRepository;
import com.store.reservation.store.repository.queryDsl.dto.SimpleStore;
import com.store.reservation.store.util.GeoCoding;
import com.store.reservation.store.util.implementation.kakao.dto.LocationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.store.reservation.store.exception.StoreErrorCode.ALREADY_REGISTERED;
import static com.store.reservation.store.exception.StoreErrorCode.NO_SUCH_STORE;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreSearchRepository storeSearchRepository;
    private final GeoCoding geoCoding;
    public void create(CreateStoreDto createStoreDto,
                       MemberInformation memberInformation) {
        if (storeRepository.existsStoreByMemberInformation(memberInformation)) {
            throw new StoreException(ALREADY_REGISTERED);
        }
        LocationDto locationDto = geoCoding.convertToCoordinateFrom(createStoreDto.getRoadName());
        Location location = Location.createBy(locationDto);
        OperatingHours operatingHours = OperatingHours.createBy(createStoreDto.getTimeDto());
        operatingHours.defineReservationTimes();

        storeRepository.save(Store.builder()
                .storeName(createStoreDto.getStoreName())
                .memberInformation(memberInformation)
                .location(location)
                .operatingHours(operatingHours)
                .foods(createStoreDto.getFoodListToSet())
                .build());
    }

    public void update(UpdateStoreDto storeDto, Long storeId,
                       MemberInformation memberInformation) {
        LocationDto locationDto = geoCoding.convertToCoordinateFrom(storeDto.getRoadName());
        Store store = storeRepository.findByIdAndMemberInformation(storeId, memberInformation)
                .orElseThrow(() -> new StoreException(StoreErrorCode.UPDATE_BEFORE_CREATE_STORE));
        store.update(storeDto, locationDto);
        storeRepository.save(store);
    }

    public Store searchStoreByCustomer(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new StoreException(NO_SUCH_STORE));
    }

    public Store searchStoreByOwner(MemberInformation memberInformation, Long storeId) {
        return storeRepository.findByIdAndMemberInformation(storeId, memberInformation)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));
    }

    // todo
    // 매장 탐색은 비회원이더라도 가능하다.
    // 따라서 필요한 정보는 현재 위치, 반경과 검색 조건이다.
    // 여기서 고려해야할 점은 현재 위치 주변도 가능하고 검색하고자 하는 지역이 될 수도 있다.
    public Page<SimpleStore> searchStoreListBy(SearchStoreDto searchStoreDto, Pageable pageable) {
        return storeSearchRepository.searchStoreByCondition(searchStoreDto, pageable).map(SimpleStore::from);
    }

}
