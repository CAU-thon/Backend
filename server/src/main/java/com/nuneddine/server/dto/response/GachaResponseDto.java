package com.nuneddine.server.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nuneddine.server.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class GachaResponseDto {
    @JsonProperty("item")
    private Item item;

    @JsonProperty("gachable")
    private boolean gachable;
}
