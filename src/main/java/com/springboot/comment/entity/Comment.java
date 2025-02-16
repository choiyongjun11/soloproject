package com.springboot.comment.entity;

import com.springboot.board.entity.Board;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에 AUTO_INCREMENT 사용
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean secret; //공개글, 비밀글 여부를 저장

    @Column(nullable = false)
    private String createdBy;  // 답변 작성자 -> 관리자

    //board 와의 관계 매핑 board(1) <-> comment(N)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board-id", nullable = false)
    private Board board;

    @Builder.Default // 빌더 패턴 사용 시 기본값으로 현재 시간(LocalDateTime.now())을 설정
    private LocalDateTime createdAt = LocalDateTime.now(); // 등록 날짜 자동 설정



}
