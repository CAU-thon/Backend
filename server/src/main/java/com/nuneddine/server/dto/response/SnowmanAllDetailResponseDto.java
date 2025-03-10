package com.nuneddine.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SnowmanAllDetailResponseDto {
    private Long id;
    private String name;
    private String image; // 눈사람 이미지
    private int mapNumber; // 눈사람이 있는 맵
    private double posX; // 눈사람의 위치 x좌표
    private double posY; // 눈사람의 위치 y좌표
    private Long userId; // 해당 눈사람을 만든 사용자 id

    private String quiz; // 눈사람 문제 내용
    private Long answerId; // 눈사람 문제 답
    private String content1;
    private String content2;
    private String content3;

    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
