package com.nuneddine.server.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SnowmanItemRequest {
    private Long id;
    private double posX;
    private double posY;
    private double posZ;
}
