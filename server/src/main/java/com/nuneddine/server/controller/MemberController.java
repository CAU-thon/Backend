package com.nuneddine.server.controller;

import com.nuneddine.server.domain.Member;
import com.nuneddine.server.dto.request.UpdateUsernameRequestDto;
import com.nuneddine.server.dto.response.MemberInfoResponseDto;
import com.nuneddine.server.dto.response.UpdateUsernameResponseDto;
import com.nuneddine.server.service.JwtService;
import com.nuneddine.server.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final JwtService jwtService;
    private final MemberService memberService;

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
                .build();

        return ResponseEntity.ok(responseDto);
    }

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
