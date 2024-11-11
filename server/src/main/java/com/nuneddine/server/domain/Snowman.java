package com.nuneddine.server.domain;

import com.nuneddine.server.service.SnowmanService;
import jakarta.persistence.*;
import lombok.*;

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

    private String name;
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

    @Builder
    Snowman(String name, String color, SnowmanShape snowmanShape, String image, int mapNumber, double posX, double posY, String quiz, Long answerId, Member member) {
        this.name = name;
        this.color = color;
        this.snowmanShape = snowmanShape;
        this.image = image;
        this.mapNumber = mapNumber;
        this.posX = posX;
        this.posY = posY;
        this.quiz = quiz;
        this.answerId = answerId;
        this.member = member;
    }
}
