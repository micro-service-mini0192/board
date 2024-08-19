package com.example.demo.board.presentation.dto;

import com.example.demo.board.domain.Board;
import com.example.demo.security.member.Member;
import lombok.Builder;

public class BoardResponse {
    @Builder
    public record FindById(
            String id,
            String title,
            String description,
            Member member
    ){
        public static FindById toDto(Board board) {
            return FindById.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .description(board.getDescription())
                    .member(board.getMember())
                    .build();
        }
    }

    @Builder
    public record FindAll(
            String id,
            String title,
            MemberInfo member

    ) {
        @Builder
        public record MemberInfo(
                Long id,
                String nickname
        ){}

        public static FindAll toDto(Board board) {
            MemberInfo member = MemberInfo.builder()
                    .id(board.getMember().getId())
                    .nickname(board.getMember().getNickname())
                    .build();

            return FindAll.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .member(member)
                    .build();
        }
    }
}
