package com.store.reservation.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.store.reservation.member.validation.email.Email;
import com.store.reservation.member.validation.password.Password;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInDto {
    @Email
    private String email;
    @Password
    private String password;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss", timezone = "Asia/Seoul")
    @NotBlank
    private Date today;
}
