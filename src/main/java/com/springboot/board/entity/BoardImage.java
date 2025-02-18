//package com.springboot.board.entity;
//
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name ="board_image")
//
//
//25.02.18 15:18 잘못 구현된 것으로 판단되어 사용을 금지 합니다. 최용준 (인)
//
//public class BoardImage {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "image_path", nullable = false)
//    private String imagePath; //사진 경로
//
//    @ManyToOne //N(board) : 1(BoardImage) FK
//    @JoinColumn(name = "board_id", nullable = false)
//    private Board board;
//
//    public BoardImage(String imagePath, Board board) {
//        this.imagePath = imagePath;
//        this.board = board;
//    }
//
//}
