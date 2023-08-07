package com.store.reservation.store.food.domain;

import com.store.reservation.member.model.BaseEntity;
import com.store.reservation.store.store.domain.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class Food extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long foodId;
    private long price;
    private String description;
    private String category;

    @ManyToOne
    @JoinColumn(name = "storeId")
    private Store store;
}
