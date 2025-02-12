package com.springboot.member.entity;

import com.springboot.audit.Auditable;
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

    @Column(nullable = false, length = 64)
    private String password;

    @Column(length = 64, nullable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    private String phone;

    // 궈한 부여 기능
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    //생성자
    public Member(String email,String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }


}
