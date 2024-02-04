package com.store.reservation.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;

}
