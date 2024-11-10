package com.nuneddine.server.controller;

import com.nuneddine.server.config.jwt.JwtUtil;
import com.nuneddine.server.domain.Member;
import com.nuneddine.server.dto.request.KakaoOAuthRequestDto;
import com.nuneddine.server.dto.response.JwtResponseDto;
import com.nuneddine.server.service.KakaoOAuthService;
import com.nuneddine.server.service.MemberService;
import com.nuneddine.server.util.KakaoUser;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/oauth/kakao")
public class KakaoController {

    private KakaoOAuthService kakaoOAuthService;
    private MemberService memberService;
    private JwtUtil jwtUtil;

    @PostMapping("/token")
    public ResponseEntity<?> getAccessToken(@RequestBody KakaoOAuthRequestDto request) {
        String authorizationCode = request.getAuthorization_code();
        String accessToken = kakaoOAuthService.exchangeCodeForAccessToken(authorizationCode);

        KakaoUser kakaoUser = kakaoOAuthService.fetchKakaoUserInfo(accessToken);
        Long kakaoId = kakaoUser.getId();
        Member member = memberService.getMemberByKakaoId(kakaoId);
        String jwt = jwtUtil.generateToken(member.getId());

        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);

        return ResponseEntity.ok(response);
    }
}
