package com.nuneddine.server.controller;

import com.nuneddine.server.config.jwt.JwtUtil;
import com.nuneddine.server.domain.Item;
import com.nuneddine.server.domain.Member;
import com.nuneddine.server.dto.request.KakaoOAuthRequestDto;
import com.nuneddine.server.dto.response.JwtResponseDto;
import com.nuneddine.server.service.ItemService;
import com.nuneddine.server.service.KakaoOAuthService;
import com.nuneddine.server.service.MemberService;
import com.nuneddine.server.util.KakaoUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/oauth/kakao")
public class KakaoController {

    private KakaoOAuthService kakaoOAuthService;
    private MemberService memberService;
    private ItemService itemService;
    private JwtUtil jwtUtil;

    @PostMapping("/token")
    public ResponseEntity<?> getAccessToken(@RequestBody KakaoOAuthRequestDto request) {
        String authorizationCode = request.getAuthorization_code();
        String accessToken = kakaoOAuthService.exchangeCodeForAccessToken(authorizationCode);

        //accessToken -> 사용자 정보 받아오기
        KakaoUser kakaoUser = kakaoOAuthService.fetchKakaoUserInfo(accessToken);
        Member member = memberService.getMemberByKakaoId(kakaoUser.getId());
        if (itemService.getItemsByMember(member).isEmpty()) {
            itemService.setDefaultItems(member);
        }

        //받아온 Member -> jwt 만들어서 반환
        String token = jwtUtil.generateToken(member.getId());
        JwtResponseDto response = new JwtResponseDto(token);

        return ResponseEntity.ok(response);
    }
}
