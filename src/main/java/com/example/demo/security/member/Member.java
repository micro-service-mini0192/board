package com.example.demo.security.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private Long id;
    private String username;
    private String nickname;
    private List<String> role;
}
