package com.store.reservation.store.domain.model;


import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.model.BaseEntity;
import com.store.reservation.store.domain.vo.food.Food;
import com.store.reservation.store.domain.vo.location.Location;
import com.store.reservation.store.domain.vo.operating.OperatingHours;
import com.store.reservation.store.dto.common.StoreDto;
import com.store.reservation.store.util.implementation.kakao.dto.LocationDto;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Store extends BaseEntity {

    private static final long INITIAL_STAR_RATING = 0L;
    private static final long INITIAL_REVIEW_COUNT = 0L;
    private static final int INITIAL_NUMBER_OF_RESERVATION_TIME = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Builder.Default
    private long starCount = INITIAL_STAR_RATING;
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
    private int numberOfReservationPerTime = INITIAL_NUMBER_OF_RESERVATION_TIME;


    public void addFoodList(Set<Food> newFoodList) {
        this.foods.addAll(newFoodList);
    }

    public void update(StoreDto storeDto, LocationDto locationDto) {
        this.storeName = storeDto.getStoreName();
        this.operatingHours = OperatingHours.createBy(storeDto.getTimeDto());
        this.operatingHours.defineReservationTimes();
        this.location = Location.createBy(locationDto);
        this.numberOfReservationPerTime = storeDto.getNumberOfPeoplePerTime();
        addFoodList(storeDto.getFoodListToSet());
    }

    public boolean isPossibleToReserve(int numberOfCurrentReservation) {
        return this.numberOfReservationPerTime > numberOfCurrentReservation;
    }

    public boolean isCorrectReservationTime(LocalTime reservationTime) {
        return this.operatingHours.getReservationTimes().contains(reservationTime);
    }

    public void increaseReviewCount() {
        this.numberOfReviews++;
    }

    public void increaseStarCount(int starCount) {
        this.starCount += starCount;
    }

    public double getStarRating() {
        return this.starCount / (double) Math.max(1, this.numberOfReviews);
    }

    public void changeStarCount(int previous, int changed) {
        this.starCount = Math.max(0, this.starCount - previous);
        this.starCount += changed;
    }

}
