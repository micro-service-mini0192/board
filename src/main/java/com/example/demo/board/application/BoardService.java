package com.example.demo.board.application;

import com.example.demo.board.presentation.dto.BoardRequest;
import com.example.demo.board.presentation.dto.BoardResponse;
import com.example.demo.security.member.Member;

import java.util.List;

public interface BoardService {
    BoardResponse.FindById save(BoardRequest.Save takenDto, Member member);
    List<BoardResponse.FindAll> findAll();
}
