package com.nuneddine.server.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoOAuthRequestDto {
    private String authorization_code;
}
