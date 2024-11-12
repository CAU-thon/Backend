package com.nuneddine.server.dto.response;

import com.nuneddine.server.domain.Member;
import com.nuneddine.server.domain.SnowmanItem;
import com.nuneddine.server.domain.SnowmanShape;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SnowmanAllDetailResponseDto {
    private Long id;
    private String name;
    private String color;
    private SnowmanShape snowmanShape;
    private String image; // 눈사람 이미지
    private int mapNumber; // 눈사람이 있는 맵
    private double posX; // 눈사람의 위치 x좌표
    private double posY; // 눈사람의 위치 y좌표
    private Member member; // 해당 눈사람을 만든 사용자

    private List<SnowmanItem> snowmanItems;

    private String quiz; // 눈사람 문제 내용
    private Long answerId; // 눈사람 문제 답
    private String content1;
    private String content2;
    private String content3;
}
