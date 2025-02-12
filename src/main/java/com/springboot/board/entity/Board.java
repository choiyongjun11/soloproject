package com.springboot.board.entity;

import com.springboot.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    private long boardId;
    private String days;
    private String question;
    private QuestionStatus questionStatus = QuestionStatus.QUESTION_DELETED;


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
