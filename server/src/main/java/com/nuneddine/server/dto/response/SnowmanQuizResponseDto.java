package com.nuneddine.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SnowmanQuizResponseDto {
    private Long id;
    private String name;
    private String image;
    private String quiz;
    private Long answerId;
    private String choice1;
    private String choice2;
    private String choice3;
    private String username;
    private boolean isSolved;
}
