package com.store.reservation.store.store.dto.create;

import com.store.reservation.member.domain.Member;
import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.review.domain.Review;
import com.store.reservation.store.food.domain.Food;
import com.store.reservation.store.store.domain.vo.Location;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDto {
    @Builder.Default
    private double starRating=0.0;
    private String storeName;

    private Member member;

    private Location location;
    @Builder.Default
    private List<Food> foods = new ArrayList<>();
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<Reservation>();
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
}
