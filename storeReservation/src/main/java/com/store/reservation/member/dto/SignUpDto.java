package com.store.reservation.member.dto;

import com.store.reservation.member.validation.email.Email;
import com.store.reservation.member.validation.name.Name;
import com.store.reservation.member.validation.password.Password;
import com.store.reservation.member.validation.phoneNumber.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema
public class SignUpDto {
    @Email
    private String email;
    @Password
    private String password;
    @Name
    private String username;
    @PhoneNumber
    private String phoneNumber;
    @NotNull
    private List<String> roles;
}
