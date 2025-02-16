package com.springboot.comment.repository;

import com.springboot.board.entity.Board;
import com.springboot.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsByBoard(Board board); // 특정 게시글에 댓글이 있는지 확인
}
