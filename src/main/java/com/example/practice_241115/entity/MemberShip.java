package com.example.practice_241115.entity;

import com.example.practice_241115.constant.Role;
import jakarta.persistence.*;
import lombok.*;

import java.awt.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class MemberShip {
    // 자동증가하는 pk값
    // 참조명은 member_id로 지정하였음
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;

    //nullable = false 빈값 허용 안함
    @Column(nullable = false)
    private String name;
    // 이메일은 고유값으로 지정 unique
    @Column(unique = true , nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    //권한
    @Enumerated(EnumType.STRING)
    private Role role;

}
