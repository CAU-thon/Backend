package com.nuneddine.server.service;

import com.nuneddine.server.config.jwt.JwtUtil;
import com.nuneddine.server.domain.Member;
import com.nuneddine.server.dto.response.KakaoOAuthTokenResponseDto;
import com.nuneddine.server.repository.MemberRepository;
import com.nuneddine.server.util.KakaoUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public String exchangeCodeForAccessToken(String code) {
        WebClient webClient = WebClient.builder().build();

        KakaoOAuthTokenResponseDto tokenResponse = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .body(BodyInserters.fromFormData("client_id", clientId)
                        .with("code", code)
                        .with("grant_type", "authorization_code")
                        .with("redirect_uri", redirectUri))
                .retrieve()
                .bodyToMono(KakaoOAuthTokenResponseDto.class)
                .block();

        String accessToken = null;
        try {
            accessToken = tokenResponse.getAccessToken();

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return accessToken;
    }

    public KakaoUser fetchKakaoUserInfo(String accessToken) {
        KakaoUser kakaoUser = WebClient.create()
                .get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUser.class)
                .block();

        Member member = memberRepository.findByKakaoId(kakaoUser.getId())
                .orElseGet(() -> Member.builder()
                        .kakaoId(kakaoUser.getId())
                        .username(kakaoUser.getKakaoAccount().getProfile().getNickname())
                        .chance(3)
                        .build(0)
                        .point(500)
                        .image(kakaoUser.getKakaoAccount().getProfile().getImage())
                        .build());

        if (member.getId() == null) {
            memberRepository.save(member);
        }

        String image = kakaoUser.getKakaoAccount().getProfile().getImage();
        if (member.getImage() != image) {
            member.updateImage(image);
            memberRepository.save(member);
        }

        return kakaoUser;
    }

    public String createJwtForUser(KakaoUser kakaoUser) {
        return jwtUtil.generateToken(kakaoUser.getId());
    }
}
