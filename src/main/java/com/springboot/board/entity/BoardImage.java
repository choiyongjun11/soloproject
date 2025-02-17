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
