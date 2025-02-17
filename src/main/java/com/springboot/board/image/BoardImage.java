package com.springboot.board.image;


import com.springboot.board.entity.Board;
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
@Table(name ="boardImage")
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY) //N(board) : 1(BoardImage)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
}
