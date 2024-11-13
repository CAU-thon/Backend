package com.nuneddine.server.dto.request;

import com.nuneddine.server.domain.SnowmanShape;
import lombok.Getter;

import java.util.List;

@Getter
public class SnowmanUpdateRequestDto {
    private String name;
    private String color;
    private SnowmanShape snowmanShape;
    private String image;
    private List<SnowmanItemRequest> snowmanItemRequests;
}
