package com.store.reservation.member.dto;

import com.store.reservation.member.domain.Member;
import com.store.reservation.member.domain.type.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SignUp {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private String id;
        private String password;
        private String username;
        private String phoneNumber;
        private List<String> roles;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String id;
        private String username;
        private String phoneNumber;
        private String email;
        private List<String> roles;

        public static Response from(Member member){
            return Response.builder()
                    .id(member.getEmail())
                    .username(member.getName())
                    .email(member.getEmail())
                    .phoneNumber(member.getPhoneNumber())
                    .roles(member.getRoles())
                    .build();
        }
    }
}
