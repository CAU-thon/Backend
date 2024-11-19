package com.nuneddine.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberInfoResponseDto {

    private Long userId;
    private String username;
    private int build;
    private int chance;
    private int point;
    private String image;
}
