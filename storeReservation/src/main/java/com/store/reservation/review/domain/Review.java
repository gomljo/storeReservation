package com.store.reservation.review.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.model.BaseEntity;
import com.store.reservation.store.domain.model.Store;
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
    private long id;

    private String content;

    private int starRating;

    @ManyToOne
    @JoinColumn(name = "member_information_id")
    private MemberInformation memberInformation;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "storeId")
    private Store store;

    public void update(int starRating, String content){
        this.starRating = starRating;
        this.content = content;
    }

}
