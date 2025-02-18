package com.springboot.comment.repository;

import com.springboot.board.entity.Board;
import com.springboot.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsByBoard(Board board); // 특정 게시글에 댓글이 있는지 확인

    //특정 게시글에 달린 모든 댓글 리스트를 가져오는 기능
    List<Comment>  findByBoard(Board board); //board를 기준으로 댓글 목록을 조회
}
