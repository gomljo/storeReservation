package com.store.reservation.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
