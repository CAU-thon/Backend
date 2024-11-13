package com.nuneddine.server.controller;

import com.nuneddine.server.domain.Member;
import com.nuneddine.server.domain.MemberSnowman;
import com.nuneddine.server.dto.request.SnowmanUpdateRequestDto;
import com.nuneddine.server.dto.request.SolveQuizRequestDto;
import com.nuneddine.server.dto.request.SnowmanRequestDto;
import com.nuneddine.server.dto.response.SnowmanAllDetailResponseDto;
import com.nuneddine.server.dto.response.SnowmanDetailResponseDto;
import com.nuneddine.server.dto.response.SnowmanQuizResponseDto;
import com.nuneddine.server.dto.response.SnowmanResponseDto;
import com.nuneddine.server.service.JwtService;
import com.nuneddine.server.service.MemberService;
import com.nuneddine.server.service.SnowmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1") // URL 주소 매핑
public class SnowmanController {
    @Autowired
    SnowmanService snowmanService;
    @Autowired
    JwtService jwtService;
    @Autowired
    MemberService memberService;

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

    // 맵 눈사람 생성하기
    @PostMapping("/map/{mapNumber}/snowman")
    public ResponseEntity<Long> createSnowman(@RequestBody SnowmanRequestDto snowmanRequestDto, @PathVariable(value = "mapNumber") int mapNumber, @RequestHeader("Authorization") String header) {
        String token = header.substring(7);
        Long memberId = jwtService.getMemberIdFromToken(token);
        Member member = memberService.getMemberById(memberId);

        Long snowmanId = snowmanService.createSnowman(snowmanRequestDto, mapNumber, member);
        return new ResponseEntity<>(snowmanId, HttpStatus.CREATED);
    }

    // 본인이 만든 눈사람 리스트업
    @GetMapping("/my-snowman")
    public ResponseEntity<List<SnowmanDetailResponseDto>> getMySnowman(@RequestHeader("Authorization") String header) {
        String token = header.substring(7);
        Long memberId = jwtService.getMemberIdFromToken(token);
        Member member = memberService.getMemberById(memberId);

        List<SnowmanDetailResponseDto> snowmans = snowmanService.findMySnowman(member);
        if (snowmans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.ok(snowmans);
        }
    }

    // 눈사람 모든 정보 보기
    @GetMapping("/snowman/detail/{snowmanId}")
    public ResponseEntity<SnowmanAllDetailResponseDto> allDailSnowman(@PathVariable(value = "snowmanId") Long snowmanId) {
        SnowmanAllDetailResponseDto responseDto = snowmanService.allDetailSnowman(snowmanId);
        return ResponseEntity.ok(responseDto);
    }

    // 눈사람 수정하기
    @PatchMapping("/snowman/detail/{snowmanId}")
    public ResponseEntity<Long> updateSnowman(@RequestHeader("Authorization") String header, @PathVariable(value = "snowmanId") Long snowmanId, @RequestBody SnowmanUpdateRequestDto requestDto) {
        // 본인 눈사람만 수정 가능
        String token = header.substring(7);
        Long memberId = jwtService.getMemberIdFromToken(token);
        Member member = memberService.getMemberById(memberId);

        Long Id = snowmanService.updateSnowman(snowmanId, requestDto, member);
        return new ResponseEntity<>(Id, HttpStatus.CREATED);
    }

    // 눈사람 퀴즈 보기
    @GetMapping("/snowman/{snowmanId}")
    public ResponseEntity<SnowmanQuizResponseDto> getSnowmanQuiz(@PathVariable(value = "snowmanId") Long snowmanId) {
        SnowmanQuizResponseDto snowmanQuizResponseDto = snowmanService.findSnowmanQuiz(snowmanId);
        return ResponseEntity.ok(snowmanQuizResponseDto);
    }

    // 눈사람 퀴즈 맞추기
    @PostMapping("/snowman/{snowmanId}")
    public ResponseEntity<Boolean> solveSnowmanQuiz(@PathVariable(value = "snowmanId") Long snowmanId, @RequestHeader("Authorization") String header, @RequestBody SolveQuizRequestDto solveQuizRequestDto) {
        String token = header.substring(7);
        Long memberId = jwtService.getMemberIdFromToken(token);
        Member member = memberService.getMemberById(memberId);
        Long number = solveQuizRequestDto.getNumber();

        Boolean isAnswer = snowmanService.solveSnowmanQuiz(snowmanId, number, member);
        return ResponseEntity.ok(isAnswer);
    }
}
