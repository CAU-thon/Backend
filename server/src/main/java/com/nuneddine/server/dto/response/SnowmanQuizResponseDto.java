package com.nuneddine.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SnowmanQuizResponseDto {
    private Long id;
    private String name;
    private String username;
    private String image;
    private String quiz;
    private Long answerId;
    private String choice1;
    private String choice2;
    private String choice3;

    private boolean isSolved;
    private Long myAnswerId;
    private double ratio1; // 1번을 선택한 비율
    private double ratio2; // 2번을 선택한 비율
    private double ratio3; // 3번을 선택한 비율
}
