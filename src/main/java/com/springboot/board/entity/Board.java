package com.springboot.board.entity;

import com.springboot.audit.Auditable;
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
@Table(name = "board")
public class Board extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에 AUTO_INCREMENT 사용
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY) //fetch는 필요할 때만 연관된 데이터를 가져온다.
    @JoinColumn(name = "member_id", nullable = false)  //외례키 member_id로 매핑 - 다대일 관계(N:1)
    private Member member; //질문(N)을 등록한 회원(1)

    @Column(nullable = false, length = 100) //nullable 는 DB에서 NOT NULL 제약 사용
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean secret; //공개글, 비밀글 여부를 저장

    @Enumerated(value = EnumType.STRING) //Enum (상수)값을 문자열로 저장하기 위한 애너테이션
    @Column(length = 20,nullable = false)
    @Builder.Default //빌더 패턴 사용 시 기본값으로 QUESTION_REGISTERED 설정
    private QuestionStatus questionStatus = QuestionStatus.QUESTION_REGISTERED; //질문 상태 설정

    @Builder.Default // 빌더 패턴 사용 시 기본값으로 현재 시간(LocalDateTime.now())을 설정
    private LocalDateTime createdAt = LocalDateTime.now(); // 등록 날짜 자동 설정

    @Column(nullable = false) //nullable 는 DB에서 NOT NULL 제약 사용
    @Builder.Default // 빌더 패턴 사용 시 조회 수 기본값을 0으로 설정
    private long viewCount= 0; // 조회 수 초기값

    /*
      질문 상태(QuestionStatus)를 정의하는 열거형(enum)
      - 특정 질문이 현재 어떤 상태인지 나타내는 역할
      - EnumType.STRING을 사용하여 DB에 문자열로 저장됨
     */

    public enum QuestionStatus {
        QUESTION_REGISTERED("질문 등록 상태"),
        QUESTION_ANSWERED("답변 완료 상태"),
        QUESTION_DELETED("질문 삭제 상태"),
        QUESTION_DEACTIVED("질문 비활성화 상태: 회원 탈퇴 시, 질문 비활성화 상태"),
        QUESTION_SECRET("질문 비공개"),
        QUESTION_PUBLIC("질문 공개");

        @Getter
        private String status; //상태 설명을 저장하는 필드

        QuestionStatus(String status) { //생성자로 status 필드에 저장
            this.status = status;
        }
        public String getStatus() { //질문 상태값을 문자열로 저장 반환하는 기능
            return status;
        }
    }
}
