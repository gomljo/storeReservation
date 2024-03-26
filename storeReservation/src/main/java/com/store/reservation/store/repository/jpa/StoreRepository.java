package com.store.reservation.store.repository.command;


import com.store.reservation.member.memberInfo.domain.MemberInformation;
import com.store.reservation.store.domain.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    boolean existsByStoreName(String storeName);

    Optional<Store> findByStoreName(String storeName);

    Optional<Store> findById(long storeId);

    boolean existsStoreByIdAndMemberInformation(Long storeId,
                                                MemberInformation memberInformation);
    boolean existsStoreByMemberInformation(MemberInformation memberInformation);
    Optional<Store> findByMemberInformation(MemberInformation memberInformation);
}
