package com.store.reservation.store.domain.model;


import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.model.BaseEntity;
import com.store.reservation.store.domain.vo.food.Food;
import com.store.reservation.store.domain.vo.location.Location;
import com.store.reservation.store.domain.vo.operating.OperatingHours;
import com.store.reservation.store.dto.common.StoreDto;
import com.store.reservation.store.dto.update.UpdateStoreDto;
import com.store.reservation.store.util.implementation.kakao.dto.LocationDto;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Store extends BaseEntity {

    private static final Double INITIAL_STAR_RATING = 0.0;
    private static final Long INITIAL_REVIEW_COUNT = 0L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Builder.Default
    private double starRating = INITIAL_STAR_RATING;
    @NotNull
    private String storeName;
    @Builder.Default
    private Long numberOfReviews = INITIAL_REVIEW_COUNT;
    @OneToOne
    @JoinColumn(name = "member_information_id")
    private MemberInformation memberInformation;
    @ElementCollection
    @Builder.Default
    private Set<Food> foods = new HashSet<>();
    @Embedded
    private OperatingHours operatingHours;
    @Embedded
    private Location location;
    @Builder.Default
    private Long numberOfReservationPerTime=0L;


    public void addFoodList(Set<Food> newFoodList) {
        this.foods.addAll(newFoodList);
    }


    public void update(StoreDto storeDto, LocationDto locationDto) {
        this.storeName = storeDto.getStoreName();
        this.operatingHours = OperatingHours.createBy(storeDto.getTimeDto());
        this.operatingHours.defineReservationTimes();
        this.location = Location.createBy(locationDto);
        addFoodList(storeDto.getFoodListToSet());
    }

    public boolean isPossibleToReserve(Long numberOfCurrentReservation){
        return this.numberOfReservationPerTime > numberOfCurrentReservation;
    }
}
