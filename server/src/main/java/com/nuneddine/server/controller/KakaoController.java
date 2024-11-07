package com.nuneddine.server.controller;

import com.nuneddine.server.config.jwt.JwtUtil;
import com.nuneddine.server.dto.request.KakaoOAuthRequestDto;
import com.nuneddine.server.service.KakaoOAuthService;
import com.nuneddine.server.util.KakaoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/oauth/kakao")
public class KakaoController {

    @Autowired
    private KakaoOAuthService kakaoOAuthService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/token")
    public ResponseEntity<?> getAccessToken(@RequestBody KakaoOAuthRequestDto request) {
        String authorizationCode = request.getAuthorization_code();
        String accessToken = kakaoOAuthService.exchangeCodeForAccessToken(authorizationCode);

        KakaoUser kakaoUser = kakaoOAuthService.fetchKakaoUserInfo(accessToken);
        Long id = kakaoUser.getId();
        String jwt = jwtUtil.generateToken(id);

        return ResponseEntity.ok().body(Map.of("jwt", jwt));
    }
}
