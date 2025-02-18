package com.springboot.board.entity;


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
@Table(name ="board_image")


//이미지 업로드 기능과 맞지 않은 구현으로 해당 클래스 중단 시킵니다. 25-02-18 10:07 최용준 (인)

public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_path", nullable = false)
    private String imagePath; //사진 경로

    @ManyToOne //N(board) : 1(BoardImage) FK
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    public BoardImage(String imagePath, Board board) {
        this.imagePath = imagePath;
        this.board = board;
    }

}
