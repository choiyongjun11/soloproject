package com.springboot.board.controller;

import com.springboot.board.dto.BoardDto;
import com.springboot.board.entity.Board;
import com.springboot.board.mapper.BoardMapper;
import com.springboot.board.service.BoardService;

import org.apache.tomcat.jni.Multicast;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;


/*
get,post,patch,delete 기능 구성

 */
@RestController
@RequestMapping("/v1/boards")
public class BoardController {
    private final static String BOARD_DEFAULT_URL = "/v1/boards";
    private final BoardService boardService;
    private final BoardMapper mapper;

    public BoardController(BoardService boardService, BoardMapper mapper) {
        this.boardService = boardService;
        this.mapper = mapper;
    }

    //질문 기능
    @PostMapping
    public ResponseEntity postBoard(@RequestBody BoardDto.Post requestbody, @RequestParam long memberId) {
        Board board = mapper.PostDtoToBoard(requestbody);
        Board createBoard = boardService.createBoard(requestbody, memberId);
        URI location = URI.create(BOARD_DEFAULT_URL + "/" + createBoard.getBoardId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{board-id}")
    public ResponseEntity patchBoard(@PathVariable("board-id") long boardId, @RequestParam long memberId, @RequestBody BoardDto.Patch patchDto) {
        Board updatedBoard = boardService.updateBoard(boardId, memberId, patchDto);
        return ResponseEntity.ok(mapper.boardToBoardDtoResponse(updatedBoard));

    }

    @GetMapping("/{board-id}")
    public ResponseEntity getBoard(@PathVariable("board-id") long boardId, @RequestParam long memberId) {
        Board board = boardService.findBoard(boardId, memberId);
        return ResponseEntity.ok(mapper.boardToBoardDtoResponse(board));

    }

    @GetMapping //전체 조회
    public ResponseEntity<Page<BoardDto.Response>> getBoards(@RequestParam Long memberId, @RequestParam(defaultValue = "latest") String sortBy,
                                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        //boardservice에서 게시글과 댓글 정보를 포함한 결과 조회
        Page<BoardDto.Response> responsePage = boardService.findBoards(memberId, sortBy, page, size);
        return ResponseEntity.ok(responsePage);

    }

    @DeleteMapping("/{board-id}")
    public ResponseEntity deleteBoard(@PathVariable("board-id") long boardId, @RequestParam long memberId) {
        boardService.deleteBoard(boardId, memberId);
        return ResponseEntity.noContent().build();

    }




}
