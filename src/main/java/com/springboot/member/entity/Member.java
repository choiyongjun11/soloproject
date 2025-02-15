package com.springboot.member.entity;

import com.springboot.audit.Auditable;
import com.springboot.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "member")
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(length = 13, nullable = false, unique = true)
    private String phone;

    // 권한 부여 기능
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();
    //1(회원):N(질문) 관계 매핑, 부모가 변경되면 자식도 함께 변경됨 (모든 작업 전파) - cascade 기능
    //CascadeType.ALL → 회원(Member)이 저장/수정/삭제되면 게시글(Board)도 같이 저장/수정/삭제됨.
    //orphanRemoval = true → 회원이 게시글과의 관계를 끊으면, 해당 게시글은 자동으로 삭제됨.
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();


    //생성자
    public Member(String email,String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }


}
