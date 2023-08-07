package com.store.reservation.store.store.service;

import com.store.reservation.global.reservation.dto.SearchByCondition;
import com.store.reservation.global.reservation.dto.StoreDto;
import com.store.reservation.member.domain.Member;
import com.store.reservation.store.food.domain.Food;
import com.store.reservation.store.store.domain.vo.Location;
import com.store.reservation.store.store.domain.model.Store;
import com.store.reservation.store.store.dto.create.CreateStore;
import com.store.reservation.store.store.dto.update.UpdateStore;
import com.store.reservation.store.store.exception.StoreErrorCode;
import com.store.reservation.store.store.exception.StoreException;
import com.store.reservation.store.store.repository.StoreRepository;
import com.store.reservation.store.store.util.GeoCoding;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.store.reservation.store.store.exception.StoreErrorCode.NO_SUCH_STORE;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final GeoCoding geoCoding;

    public Location convertCoordinateFrom(String roadName) {
        return geoCoding.convertToCoordinateFrom(roadName);
    }

    /**
     * 점주가 운영하는 매장 정보를 등록
     * modified at: 2023-08-01
     *
     * @param request:  매장의 이름, 점주가 가입시 부여받은 식별번호를 갖는 객체
     * @param location: 매장의 도로명 주소와 x,y 좌표를 갖는 객체
     * @param manager:  매장 회원 객체
     * @return 음식 정보를 제외한 매장의 기본 정보를 갖는 매장 객체
     */
    public Store create(CreateStore.Request request, Location location, Member manager) {
        log.info("[StoreService]: 매장 정보 등록 시작");
        if (storeRepository.existsByStoreName(request.getStoreName())) {
            log.info("[StoreService]: 매장 정보 등록 과정 문제 발생!");
            throw new StoreException(StoreErrorCode.ALREADY_REGISTERED_STORE_NAME);
        }
        log.info("[StoreService end]: 매장 정보 등록 완료");
        return storeRepository.save(Store.builder()
                .storeName(request.getStoreName())
                .foods(new ArrayList<Food>())
                .starRating(0.0)
                .location(location)
                .manager(manager)
                .build()
        );
    }

    /**
     * 매장의 위치 또는 이름이 변경해주는 함수
     * modified at: 2023-08-01
     *
     * @param request: 점주가 변경하고 싶은 매장 위치와 매장 이름을 갖는 변수
     * @return 매장의 위치와 이름이 변경된 매장 객체
     */
    public Store update(UpdateStore.Request request) {
        log.info("[StoreService]: 매장 정보 수정 시작");
        Store store = storeRepository.findByStoreName(request.getStoreName())
                .orElseThrow(() -> new StoreException(StoreErrorCode.UPDATE_BEFORE_CREATE_STORE));
        log.info("[StoreService]: 매장 정보 수정 완료");
        return storeRepository.save(Store.builder()
                .storeId(store.getStoreId())
                .storeName(request.getStoreName())
                .location(request.getLocation())
                .foods(store.getFoods())
                .starRating(store.getStarRating())
                .build());
    }

    /**
     * 매장 정보 조회 요청을 통해 전달받은 매장 회원 아이디에
     * 해당하는 매장 정보를 조회
     *
     * @param storeId: 매장 정보 조회 요청
     * @return
     */
    public Store search(long storeId) {
        log.info("[StoreService]: 매장 정보 조회");
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(NO_SUCH_STORE));
    }

    public List<StoreDto> searchByDistance(Pageable pageable, SearchByCondition.Request request) {
        List<Store> stores = storeRepository.searchStoreByRadiusDistanceASC(
                request.getLat(), request.getLnt(), request.getRadius(), pageable);
        return stores.stream().map(StoreDto::fromEntity).collect(Collectors.toList());
    }

    public List<StoreDto> searchByAlphabetic(Pageable pageable, SearchByCondition.Request request) {
        List<Store> storesSortingByAlphabetic = storeRepository.searchStoreByRadiusAlphabeticASC(
                request.getLat(), request.getLnt(), request.getRadius(), pageable);
        return storesSortingByAlphabetic.stream().map(StoreDto::fromEntity).collect(Collectors.toList());
    }

    public List<StoreDto> searchByStarRating(Pageable pageable, SearchByCondition.Request request) {
        List<Store> storesSortingByStarRating = storeRepository.searchStoreByRadiusStarRatingDESC(
                request.getLat(), request.getLnt(), request.getRadius(), pageable);
        return storesSortingByStarRating.stream().map(StoreDto::fromEntity).collect(Collectors.toList());
    }

    public void updateStarRating(Store store, int starRating) {
        Store updatedStore = store.calculateStarRating(starRating);
        storeRepository.save(updatedStore);
    }
}
