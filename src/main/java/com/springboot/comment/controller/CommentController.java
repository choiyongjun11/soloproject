package com.springboot.comment.controller;

import com.springboot.comment.dto.CommentDto;
import com.springboot.comment.entity.Comment;
import com.springboot.comment.mapper.CommentMapper;
import com.springboot.comment.repository.CommentRepository;
import com.springboot.comment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/comment")
public class CommentController {
    private final static String COMMENT_DEFAULT_URL = "/v1/comment";
    private final CommentService commentService;
    private final CommentMapper mapper;

    public CommentController(CommentService commentService, CommentMapper mapper) {
        this.commentService = commentService;
        this.mapper = mapper;
    }

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommentDto.Response> postComment(@RequestBody CommentDto.Post postDto, @RequestParam long boardId, @RequestParam String email){
        //댓글 수정 서비스 호출
        Comment comment = commentService.createComment(boardId, email, postDto);

        // 생성된 댓글을 DTO로 변환
        CommentDto.Response responseDto = mapper.commentToCommentDtoResponse(comment);

        // 성공 응답 반환 (DTO를 반환)
        return ResponseEntity.ok(responseDto);
    }

    // 댓글 수정
    @PatchMapping("/{comment-id}")
    public ResponseEntity<CommentDto.Response> patchComment(@PathVariable("comment-id") long commentId,@RequestParam String email, @RequestBody CommentDto.Patch patchDto) {
        // 댓글 수정 서비스 호출
        Comment comment = commentService.updateComment(commentId, email, patchDto);

        // 수정된 댓글을 DTO로 변환
        CommentDto.Response responseDto = mapper.commentToCommentDtoResponse(comment);

        // 성공 응답 반환 (DTO를 반환)
        return ResponseEntity.ok(responseDto);
    }

    // 댓글 삭제
    @DeleteMapping("/{comment-id}")
    public ResponseEntity<String> deleteComment(@PathVariable("comment-id") long commentId, @RequestParam String email) {

        // 댓글 삭제 서비스 호출
        commentService.deleteComment(commentId, email);

        // 성공 메시지 반환
        return ResponseEntity.ok("Comment successfully deleted.");
    }

}
