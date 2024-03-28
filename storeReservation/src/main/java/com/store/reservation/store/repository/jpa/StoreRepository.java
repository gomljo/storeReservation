package com.store.reservation.store.repository.jpa;


import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.store.domain.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    boolean existsByStoreName(String storeName);

    Optional<Store> findByStoreName(String storeName);


    boolean existsStoreByIdAndMemberInformation_Id(Long storeId,Long managerId);
    boolean existsStoreByMemberInformation(MemberInformation memberInformation);


    Optional<Store> findByMemberInformation(MemberInformation memberInformation);
    Optional<Store> findByIdAndMemberInformation(Long storeId, MemberInformation memberInformation);
}
