package com.example.demo.security.member.dto;

import lombok.Builder;

public class AuthenticationResponse {
    @Builder
    public record JwtTokenReissue(
            String refreshToken
    ){}
}
