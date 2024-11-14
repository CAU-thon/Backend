package com.nuneddine.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SnowmanResponseDto {
    private Long id;
    private String image;
    private double posX;
    private double posY;
}
