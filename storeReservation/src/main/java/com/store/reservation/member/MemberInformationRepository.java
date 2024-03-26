package com.store.reservation.member;

import com.store.reservation.member.memberInfo.domain.MemberInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberInformationRepository extends JpaRepository<MemberInformation, Long> {

    Optional<MemberInformation> findByEmail(String email);
    boolean existsByEmail(String email);
}
