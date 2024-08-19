package com.example.demo.security.member.dto;

import lombok.Builder;

public class AuthenticationRequest {
    @Builder
    public record JwtToken(
            String jwtToken
    ){}
}
