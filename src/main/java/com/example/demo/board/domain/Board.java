package com.example.demo.board.domain;

import com.example.demo.security.member.Member;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@Document(collection = "board")
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    private String id;
    private String title;
    private String description;
    private Member member;
}
