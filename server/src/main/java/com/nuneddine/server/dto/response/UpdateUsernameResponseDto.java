package com.nuneddine.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUsernameResponseDto {
    private Long id;
    private String username;
}
