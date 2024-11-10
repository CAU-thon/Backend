package com.nuneddine.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Snowman extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "snowmanId")
    private Long id;

    private String color;
    private SnowmanShape snowmanShape;
    private String image; // 눈사람 이미지
    private int mapNumber; // 눈사람이 있는 맵
    private double posX; // 눈사람의 위치 x좌표
    private double posY; // 눈사람의 위치 y좌표

    private String quiz; // 눈사람 문제 내용
    private Long answerId; // 눈사람 문제 답

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "memberId")
    private Member member; // 해당 눈사람을 만든 사용자
}
