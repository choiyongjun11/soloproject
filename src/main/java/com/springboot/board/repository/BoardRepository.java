package com.springboot.board.repository;

import com.springboot.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository <Board,Object> {
    List<Board> findAllByBoard(Board.QuestionStatus questionStatus, Pageable pageable); //전체 질문 조회
    Optional<Board> findByBoard(long boardId, Board.QuestionStatus questionStatus);

}
