package com.store.reservation.member.security.jwt;

import com.store.reservation.member.security.userDetails.SecurityUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Jwt {
    private final String secretKey;
    private static final String KEY_ROLES = "roles";


    public Jwt(@Value("${spring.jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String generate(SecurityUser securityUser, Date today, Date expiration) {
        Claims claims = Jwts.claims().setSubject(securityUser.getUsername());
        claims.put(KEY_ROLES, securityUser.getAuthorities());
        claims.setId(String.valueOf(securityUser.getId()));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(today)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();
    }

    public String getUserName(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException expiredJwtException) {
            return expiredJwtException.getClaims();
        }
    }

    public Date isTokenExpired(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration();
    }

}
