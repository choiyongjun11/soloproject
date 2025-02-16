package com.springboot.like.controller;

import com.springboot.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{board-id}")
    public ResponseEntity<Void> toggleLike(@PathVariable("board-id") Long boardId,
                                           @RequestParam Long memberId) {
        likeService.toggleLike(boardId, memberId);
        return ResponseEntity.ok().build();
    }
}
