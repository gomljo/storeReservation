package com.store.reservation.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.store.reservation.member.validation.email.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReissueTokenDto {
    @Email
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime today;

    public Date getDate(){
        return java.sql.Timestamp.valueOf(today);
    }
}
