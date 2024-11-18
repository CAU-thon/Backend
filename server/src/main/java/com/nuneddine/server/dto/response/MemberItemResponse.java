package com.nuneddine.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberItemResponse {
    boolean available;
    Long itemId;
    String itemName;
}
