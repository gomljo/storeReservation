package com.store.reservation.store.store.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.store.reservation.member.domain.Member;
import com.store.reservation.member.model.BaseEntity;
import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.review.domain.Review;
import com.store.reservation.store.food.domain.Food;
import com.store.reservation.store.store.domain.vo.Location;
import com.store.reservation.store.store.dto.update.UpdateStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long storeId;

    @Builder.Default
    private double starRating = 0.0;
    private String storeName;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member manager;

    @Embedded
    private Location location;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Food> foods = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    private boolean isChangeLocation(Location location){
        return location.equals(this.location);
    }
    private boolean isChangeStoreName(String storeName){
        return Objects.equals(storeName, this.storeName);
    }

    public void update(UpdateStore.Request request) {
        if (isChangeStoreName(request.getStoreName())) {
            this.storeName = request.getStoreName();
        }

        if (isChangeLocation(request.getLocation())) {
            this.location = request.getLocation();
        }
    }

    public Store calculateStarRating(int starRating){

        if(this.reviews.size()==0){
            this.starRating = starRating * 1.0;
            return this;
        }

        double updatedTotalStars = this.starRating * (this.reviews.size()-1) + starRating;
        this.starRating = updatedTotalStars / this.reviews.size();
        return this;
    }

}
