package com.nuneddine.server.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    public Member(Long kakaoId, String username, String nickname, int build, int chance, int point) {
        this.kakaoId = kakaoId;
        this.username = username;
        this.nickname = nickname;
        this.build = build;
        this.chance = chance;
        this.point = point;
    }
}
