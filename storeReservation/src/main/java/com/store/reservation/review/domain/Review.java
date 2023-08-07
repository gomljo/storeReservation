package com.store.reservation.review.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.store.reservation.member.model.BaseEntity;
import com.store.reservation.store.store.domain.model.Store;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;

    private String review;

    private int starRating;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "storeId")
    private Store store;


}
