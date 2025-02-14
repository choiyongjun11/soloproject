package com.springboot.board.entity;

import com.springboot.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long memberId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private QuestionStatus questionStatus = QuestionStatus.QUESTION_DELETED;

    @Column(nullable = false)
    private boolean secret;

    @Column(nullable = false)
    private String date;


    @Column(nullable = false)
    private long viewCount;


    public enum QuestionStatus {
        QUESTION_REGISTERED("질문 등록 상태"),
        QUESTION_ANSWERED("답변 완료 상태"),
        QUESTION_DELETED("질문 삭제 상태"),
        QUESTION_DEACTIVED("질문 비활성화 상태: 회원 탈퇴 시, 질문 비활성화 상태");

        private String status;
        QuestionStatus(String status) {
            this.status = status;
        }

        }
}
