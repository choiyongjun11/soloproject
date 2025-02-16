package com.springboot.like.entity;

import com.springboot.board.entity.Board;
import com.springboot.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    //매핑
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 좋아요를 누른 사용자

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board; // 좋아요가 눌린 게시글

    private boolean liked; // 좋아요 상태 (true: 좋아요, false: 좋아요 취소)

}
