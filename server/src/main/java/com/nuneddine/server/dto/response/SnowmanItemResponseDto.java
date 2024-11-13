package com.nuneddine.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SnowmanItemResponseDto {
    private Long id;
    private double posX;
    private double posY;
    private double posZ;
}
