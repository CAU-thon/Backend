package com.nuneddine.server.controller;

import com.nuneddine.server.service.JwtService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cors")
public class corsController {

    @Autowired
    private JwtService jwtService;

    @GetMapping("")
    public ResponseEntity<?> corsGet() {
        return ResponseEntity.ok(new Tempo("GET method success"));
    }

    @PostMapping("/{message}")
    public ResponseEntity<?> corsPost(@PathVariable("message") String message) {
        Tempo tempo = new Tempo(message);
        return ResponseEntity.ok(tempo);
    }

    @GetMapping("/jwt")
    public ResponseEntity<?> jwtAvailable(@RequestHeader("Authorization") String header) {
        String token = header.substring(7);
        System.out.println(token);
        Long kakaoId = jwtService.getKakaoIdFromToken(token);

        return ResponseEntity.ok(new Tempo(String.valueOf(kakaoId)));
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
