package com.springboot.comment.dto;

import com.springboot.board.entity.Board;
import com.springboot.validator.NotSpace;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotSpace
        private String content;
        private boolean secret;

    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        private long commentId;
        @NotSpace
        private String content;
        private boolean secret;

    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private long commentId;
        private String content;
        private boolean secret;
        private String createdBy;
        private LocalDateTime createdAt;
        private Board.QuestionStatus questionStatus; //답변 등록 시 질문 상태 변경

    }


}
