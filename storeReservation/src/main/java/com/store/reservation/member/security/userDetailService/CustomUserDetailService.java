package com.store.reservation.member.security.userDetailService;

import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.repository.MemberInformationRepository;
import com.store.reservation.member.security.userDetails.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberInformationRepository memberInformationRepository;

    @Override
    public SecurityUser loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberInformation memberInformation = memberInformationRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return new SecurityUser(memberInformation);
    }
}
