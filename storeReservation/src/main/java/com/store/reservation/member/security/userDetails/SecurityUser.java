package com.store.reservation.member.security.userDetails;

import com.store.reservation.member.domain.MemberInformation;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@Getter
@Builder
@RequiredArgsConstructor
public class SecurityUser implements UserDetails {

    private final MemberInformation memberInformation;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.memberInformation.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return this.memberInformation.getId();
    }

    @Override
    public String getPassword() {
        return this.memberInformation.getPassword();
    }

    @Override
    public String getUsername() {
        return this.memberInformation.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public boolean isNotSameUser(Long id) {
        return !this.getId().equals(id);
    }
}
