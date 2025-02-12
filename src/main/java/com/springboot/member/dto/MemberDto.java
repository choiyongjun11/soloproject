package com.springboot.member.dto;

import com.springboot.validator.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;


public class MemberDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotSpace
        @Email
        private String email;

        @NotSpace
        private String password;

        @NotSpace
        private String name;

        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$", message = "휴대폰 번호 11자리로 구성되어 있으며 '-' 형태로 되어야 합니다.")
        private String phone;

    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        private long memberId;

        @NotSpace
        private String name;

        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$", message = "휴대폰 번호 11자리로 구성되어 있으며 '-' 형태로 되어야 합니다.")
        private String phone;

        public void setMemberId(long memberId) {
            this.memberId = memberId;
        }

    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private long memberId;
        private String email;
        private String name;
        private String phone;
    }
}
