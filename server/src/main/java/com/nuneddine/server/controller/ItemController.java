package com.nuneddine.server.controller;

import com.nuneddine.server.domain.Item;
import com.nuneddine.server.domain.Member;
import com.nuneddine.server.dto.response.MemberItemResponse;
import com.nuneddine.server.repository.MemberRepository;
import com.nuneddine.server.service.ItemService;
import com.nuneddine.server.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/item")
public class ItemController {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ItemService itemService;

    private Member getMember(String header) {
        String token = header.substring(7);
        Long memberId = jwtService.getMemberIdFromToken(token);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 멤버가 존재하지 않습니다."));
        return member;
    }

    @Operation(summary = "사용할 수 있는 아이템 조회 api", description = "멤버 별 해금되어 사용할 수 있는 아이템 목록을 반환")
    @GetMapping("/inventory")
    public ResponseEntity<List<MemberItemResponse>> getInventory(@RequestHeader("Authorization") String header) {
        Member member = getMember(header);

        return new ResponseEntity<>(itemService.getAllMemberItem(member), HttpStatus.OK);
    }

    @Operation(summary = "아이템 가챠 api", description = "보유하지 않은 아이템 중 랜덤으로 아이템을 해금")
    @GetMapping("/gacha")
    public ResponseEntity<Item> gachaItem(@RequestHeader("Authorization") String header) {
        Member member = getMember(header);
        Item item = itemService.gachaItem(member);

        if(item == null) {
            // 204 No Content
            return ResponseEntity.noContent().build();
        }
        // 200 OK
        return new ResponseEntity<>(item, HttpStatus.OK);
    }
}
