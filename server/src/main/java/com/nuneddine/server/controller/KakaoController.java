package com.nuneddine.server.controller;

import com.nuneddine.server.config.jwt.JwtUtil;
import com.nuneddine.server.domain.Member;
import com.nuneddine.server.dto.request.KakaoOAuthRequestDto;
import com.nuneddine.server.dto.response.JwtResponseDto;
import com.nuneddine.server.service.ItemService;
import com.nuneddine.server.service.KakaoOAuthService;
import com.nuneddine.server.service.MemberService;
import com.nuneddine.server.util.KakaoUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/oauth/kakao")
public class KakaoController {

    private KakaoOAuthService kakaoOAuthService;
    private MemberService memberService;
    private ItemService itemService;
    private JwtUtil jwtUtil;

    @Operation(summary = "jwt 토큰 발급 api", description = "인가 코드를 사용하여 카카오 사용자 조회 및 jwt 토큰 반환")
    @PostMapping("/token")
    public ResponseEntity<?> getAccessToken(@RequestBody KakaoOAuthRequestDto request) {
        String authorizationCode = request.getAuthorization_code();
        String accessToken = kakaoOAuthService.exchangeCodeForAccessToken(authorizationCode);

        //accessToken -> 사용자 정보 받아오기
        KakaoUser kakaoUser = kakaoOAuthService.fetchKakaoUserInfo(accessToken);
        Member member = memberService.getMemberByKakaoId(kakaoUser.getId());

        //받아온 Member -> jwt 만들어서 반환
        String token = jwtUtil.generateToken(member.getId());

        JwtResponseDto response;

        //첫 로그인인 경우 기본 아이템 추가
        if (itemService.getItemsByMember(member).isEmpty()) {
            response = new JwtResponseDto(token, true);
            itemService.setDefaultItems(member);
        } else {
            response = new JwtResponseDto(token, false);
        }

        return ResponseEntity.ok(response);
    }
}
