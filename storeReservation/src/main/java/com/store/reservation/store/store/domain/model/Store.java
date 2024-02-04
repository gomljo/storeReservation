package com.store.reservation.store.store.domain.model;

import com.store.reservation.member.manager.persistance.entity.Manager;
import com.store.reservation.member.memberInfo.model.BaseEntity;
import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.review.domain.Review;
import com.store.reservation.store.food.domain.Food;
import com.store.reservation.store.store.domain.vo.location.Address;
import com.store.reservation.store.store.domain.vo.location.Location;
import com.store.reservation.store.store.domain.vo.operatingHours.OperatingHours;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Food> foods = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "store",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @Embedded
    private OperatingHours operatingHours;

    @Embedded
    private Address address;

    public Store calculateStarRating(int starRating) {

        if (this.reviews.size() == 0) {
            this.starRating = starRating * 1.0;
            return this;
        }

        double updatedTotalStars = this.starRating * (this.reviews.size() - 1) + starRating;
        this.starRating = updatedTotalStars / this.reviews.size();
        return this;
    }

}
