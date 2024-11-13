package com.nuneddine.server.controller;

import com.nuneddine.server.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cors")
@Tag(name = "Cors Test Controller", description = "cors 테스트와 jwt 토큰 확인을 위한 API")
public class corsController {

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "cors GET 테스트", description = "GET method success 라는 메시지를 반환 받은 경우 성공입니다.")
    @GetMapping("")
    public ResponseEntity<?> corsGet() {
        return ResponseEntity.ok(new Tempo("GET method success"));
    }

    @Operation(summary = "cors POST 테스트", description = "/api/v1/cos/~: ~ 부분에 넣은 메시지를 반환 받은 경우 성공입니다.")
    @PostMapping("/{message}")
    public ResponseEntity<?> corsPost(@PathVariable("message") String message) {
        Tempo tempo = new Tempo(message);
        return ResponseEntity.ok(tempo);
    }

    @Operation(summary = "jwt 유효성 검증 테스트", description = "jwt에 저장된 사용자의 PK id를 반환합니다.")
    @GetMapping("/jwt")
    public ResponseEntity<?> jwtAvailable(@RequestHeader("Authorization") String header) {
        String token = header.substring(7);
        Long id = jwtService.getMemberIdFromToken(token);

        return ResponseEntity.ok(new Tempo(String.valueOf(id)));
    }

    @Getter
    @RequiredArgsConstructor
    private static class Tempo {
        String messsage;

        public Tempo(String messsage) {
            this.messsage = messsage;
        }
    }
}
