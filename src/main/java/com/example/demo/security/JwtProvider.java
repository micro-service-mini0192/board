package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.security.member.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class JwtProvider {
    @Value("${JWTKey}")
    public String SECRET;

    public final String TOKEN_PREFIX = "Bearer ";
    public final String JWT_HEADER_STRING = "Authorization";

    public Member decodeToken(String token, String key) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(key))
                .build()
                .verify(token);

        Long id = decodedJWT.getClaim("id").asLong();
        String username = decodedJWT.getClaim("username").toString().replace("\"", "");;
        String nickname = decodedJWT.getClaim("nickname").toString().replace("\"", "");;
        List<String> role = decodedJWT.getClaim("role").asList(String.class);

        return Member.builder()
                .id(id)
                .username(username)
                .nickname(nickname)
                .role(role)
                .build();
    }
}
