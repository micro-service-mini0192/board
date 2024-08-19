package com.example.demo.board.presentation;

import com.example.demo.board.application.BoardService;
import com.example.demo.board.presentation.dto.BoardRequest;
import com.example.demo.board.presentation.dto.BoardResponse;
import com.example.demo.security.SecurityUtil;
import com.example.demo.security.member.Member;
import com.example.demo.security.member.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/v1")
@Tag(name = "Board API")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    @Operation(summary = "Join", description = "Create member")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Creation completed"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<BoardResponse.FindById> save(@Parameter(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                       @Valid @RequestBody BoardRequest.Save takenDto,
                                                       HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: “Create Board” API call", ip);

        Member member = SecurityUtil.getCurrentUserId();

        BoardResponse.FindById rtn = boardService.save(takenDto, member);
        return ResponseEntity.status(HttpStatus.CREATED).body(rtn);
    }

    @GetMapping
    @Operation(summary = "Join", description = "Create member")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Creation completed"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    public ResponseEntity<List<BoardResponse.FindAll>> findAll(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        log.info("{}: “Find All Board” API call", ip);

        List<BoardResponse.FindAll> rtn = boardService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(rtn);
    }
}
