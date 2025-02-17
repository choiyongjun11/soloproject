package com.springboot.board.repository;

import com.springboot.board.entity.Board;
import com.springboot.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    List<BoardImage> findByBoard(Board board);
}

