package com.example.demo.board.application;

import com.example.demo.board.domain.Board;
import com.example.demo.board.domain.BoardRepository;
import com.example.demo.board.presentation.dto.BoardRequest;
import com.example.demo.board.presentation.dto.BoardResponse;
import com.example.demo.security.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public BoardResponse.FindById save(BoardRequest.Save takenDto, Member member) {
        Board takenEntity = BoardRequest.Save.toEntity(takenDto, member);
        boardRepository.save(takenEntity);
        BoardResponse.FindById rtn = BoardResponse.FindById.toDto(takenEntity);
        return rtn;
    }

    @Override
    public List<BoardResponse.FindAll> findAll() {
        List<Board> savedEntity = boardRepository.findAll();
        List<BoardResponse.FindAll> rtn = savedEntity.stream().map(BoardResponse.FindAll::toDto).toList();
        return rtn;
    }
}
