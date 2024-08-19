package com.example.demo.security.filter;


import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.demo.Test;
import com.example.demo.security.JwtProvider;
import com.example.demo.security.member.Member;
import com.example.demo.security.member.MemberDetails;
import com.example.demo.security.member.dto.AuthenticationRequest;
import com.example.demo.security.member.dto.AuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RestTemplate restTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String refreshTokenHeader = request.getHeader(jwtProvider.REFRESH_HEADER_STRING);

        if(refreshTokenHeader != null) {
            AuthenticationResponse.JwtTokenReissue jwtReissue = AuthenticationResponse.JwtTokenReissue.builder()
                    .refreshToken(refreshTokenHeader)
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            String json = objectMapper.writeValueAsString(jwtReissue);
            HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

            try {
                ResponseEntity<AuthenticationRequest.JwtToken> data = restTemplate.postForEntity(
                        "http://member/v1/refresh-token",
                        requestEntity,
                        AuthenticationRequest.JwtToken.class);

                String jwtToken = Objects.requireNonNull(data.getBody()).jwtToken();
                response.addHeader(jwtProvider.JWT_HEADER_STRING, jwtToken);

                jwtToken = jwtToken.replace(jwtProvider.TOKEN_PREFIX, "");
                Member jwtTokenMember = jwtProvider.decodeToken(jwtToken, jwtProvider.SECRET);
                MemberDetails jwtTokenMemberDetails = new MemberDetails(jwtTokenMember);

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(jwtTokenMemberDetails, null, jwtTokenMemberDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (RestClientException e) {
                log.error(e.getMessage());
            }
            filterChain.doFilter(request, response);
            return;
        }

        /*
        Check JWT Token
        If header has JWT token
        Step 1: Attempt to decrypt JWT token
        Step 2: If decrypt is fails, return 401 or 400
         */
        String jwtTokenHeader = request.getHeader(jwtProvider.JWT_HEADER_STRING);
        if(jwtTokenHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtTokenHeader = jwtTokenHeader.replace(jwtProvider.TOKEN_PREFIX, "");

        try {
            Member jwtTokenMember = jwtProvider.decodeToken(jwtTokenHeader, jwtProvider.SECRET);
            MemberDetails jwtTokenMemberDetails = new MemberDetails(jwtTokenMember);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(jwtTokenMemberDetails, null, jwtTokenMemberDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch(TokenExpiredException e) {


        } catch (JWTDecodeException e) {
            log.info("JWT token is invalid");

        } catch (JWTVerificationException e) {
            log.info("Authentication failed");

        }

        filterChain.doFilter(request, response);
    }
}
