package com.nuneddine.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SnowmanDetailResponseDto {
    private Long id;
    private String name;
    private String image;
    private int correctCount; // 맞춘 사람
    private int incorrectCount; // 틀린 사람
}
