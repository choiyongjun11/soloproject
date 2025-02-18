package com.springboot.like.controller;

import com.springboot.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/likes")
//좋아요 likecounts: 1 형식으로 구현하기
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{board-id}")
    public ResponseEntity<Integer> toggleLike(@PathVariable("board-id") Long boardId,
                                              @RequestParam Long memberId) {
        Integer likeCount = likeService.toggleLike(boardId, memberId);
        return ResponseEntity.ok(likeCount); // 0 또는 1 반환
    }

}
