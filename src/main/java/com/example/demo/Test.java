package com.example.demo;

import lombok.Builder;

public class Test {
    @Builder
    public record Testg(
            String refreshToken
    ) {}
}
