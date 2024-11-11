package com.nuneddine.server.dto.request;

import com.nuneddine.server.domain.SnowmanShape;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SnowmanRequestDto {
    private Long id;
    private String color;
    private SnowmanShape snowmanShape;
    private String image;
    private double posX;
    private double posY;

    private String quiz;
    private Long answerId;

    private String content1;
    private String content2;
    private String content3;
}
