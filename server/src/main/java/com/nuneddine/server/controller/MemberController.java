package com.nuneddine.server.controller;

import com.nuneddine.server.domain.Member;
import com.nuneddine.server.dto.request.UpdateUsernameRequestDto;
import com.nuneddine.server.dto.response.MemberInfoResponseDto;
import com.nuneddine.server.dto.response.UpdateUsernameResponseDto;
import com.nuneddine.server.service.JwtService;
import com.nuneddine.server.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final JwtService jwtService;
    private final MemberService memberService;

    @Operation(summary = "사용자 정보 반환 api", description = "id, 이름, 눈사람 갯수, 퀴즈 기회, 보유 포인트 프로필 사진을 반환")
    @GetMapping("")
    public ResponseEntity<?> getMemberInfo(
            @RequestHeader("Authorization") String header
    ) {
        String token = header.substring(7);
        Long memberId = jwtService.getMemberIdFromToken(token);
        Member member = memberService.getMemberById(memberId);

        MemberInfoResponseDto responseDto = MemberInfoResponseDto.builder()
                .userId(member.getId())
                .username(member.getUsername())
                .build(member.getBuild())
                .chance(member.getChance())
                .point(member.getPoint())
                .image(member.getImage())
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "사용자명 업데이트 api", description = "사용자명 변경")
    @PatchMapping("/username")
    public ResponseEntity<?> updateUsername(
            @RequestHeader("Authorization") String header,
            @RequestBody UpdateUsernameRequestDto requestDto
    ) {
        String username = requestDto.getUsername();
        System.out.println(username);
        String token = header.substring(7);
        Long memberId = jwtService.getMemberIdFromToken(token);
        UpdateUsernameResponseDto responseDto = memberService.updateMemberUsername(memberId, username);

        return ResponseEntity.ok(responseDto);
    }
}
