package com.springboot.comment.entity;

import com.springboot.board.entity.Board;
import com.springboot.member.entity.Member;
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

    //board 와의 관계 매핑 board(1) <-> comment(N)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;  // 댓글을 작성한 관리자

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean secret; //공개글, 비밀글 여부를 저장

    @Builder.Default // 빌더 패턴 사용 시 기본값으로 현재 시간(LocalDateTime.now())을 설정
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 등록 날짜 자동 설정

    //댓글 수정
    public void update(String comment) {
        this.content = content;
    }


}
