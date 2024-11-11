package com.nuneddine.server.controller;

import com.nuneddine.server.dto.response.SnowmanResponseDto;
import com.nuneddine.server.service.SnowmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1") // URL 주소 매핑
public class SnowmanController {
    @Autowired
    SnowmanService snowmanService;

    // 맵 눈사람 리스트업
    @GetMapping("/map/{mapNumber}")
    public ResponseEntity<List<SnowmanResponseDto>> getSnowmansByMapNumber(@PathVariable(value = "mapNumber") int mapNumber) {
        List<SnowmanResponseDto> snowmans = snowmanService.findSnowmansByMap(mapNumber);
        if (snowmans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.ok(snowmans);
        }
    }
}
