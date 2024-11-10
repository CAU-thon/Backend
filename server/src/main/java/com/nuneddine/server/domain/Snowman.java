package com.nuneddine.server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Snowman extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "snowmanId")
    private Long id;

    private String snowmanId;
    private String color;
    private SnowmanShape snowmanShape;
    private String quiz; // 눈사람 문제 내용
    private Long answerId; // 눈사람 문제 답
    private String image; // 눈사람 이미지
    private int map; // 눈사람이 있는 맵
    private double posX; // 눈사람의 위치 x좌표
    private double posY; // 눈사람의 위치 y좌표
}
