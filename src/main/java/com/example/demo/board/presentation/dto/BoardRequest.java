package com.example.demo.board.presentation.dto;

import com.example.demo.board.domain.Board;
import com.example.demo.security.member.Member;

public class BoardRequest {
    public record Save(
            String title,
            String description
    ){
        public static Board toEntity(Save dto, Member member) {
            return Board.builder()
                    .title(dto.title())
                    .description(dto.description())
                    .member(member)
                    .build();
        }
    }
}
