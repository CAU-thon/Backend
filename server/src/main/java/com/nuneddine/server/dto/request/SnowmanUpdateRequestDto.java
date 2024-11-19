package com.nuneddine.server.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class SnowmanUpdateRequestDto {
    private String name;
    private String color;
    private String image;
    private List<SnowmanItemRequest> snowmanItemRequests;
}
