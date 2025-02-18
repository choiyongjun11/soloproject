package com.springboot.board.dto;

import com.springboot.board.entity.Board;
import com.springboot.validator.NotSpace;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


public class BoardDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotSpace
        private String title;
        @NotSpace
        private String content;

        private boolean secret = false; //기본 값(공개글) 으로 지정해야함(secret, no secret)

        private List<MultipartFile> images;

    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        private long boardId;
        @NotSpace
        private String title;
        @NotSpace
        private String content;
        @NotSpace
        private boolean secret; //patch user 계정만 수정가능 하도록 해야한다.

    }

    @Getter
    @AllArgsConstructor
    public static class Response {

        private long boardId;

        private String title;

        private String content;

        private boolean secret; //user 계정으로 수정 시 변경된 내용을 반영해서 응답해주기

        private Board.QuestionStatus questionStatus;

        private LocalDateTime createdAt;

        private List<String> imageUrls; //업로드

        private long viewCount;

    }


}
