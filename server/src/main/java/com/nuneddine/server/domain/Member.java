package com.nuneddine.server.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "memberId")
    private Long id;

    @Column(unique = true)
    private Long kakaoId;
    private String username;
    private String nickname;
    private int build;
    private int chance;
    private int point;
}
