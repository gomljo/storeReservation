package com.store.reservation.auth.refreshToken.repository;

import com.store.reservation.auth.refreshToken.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
