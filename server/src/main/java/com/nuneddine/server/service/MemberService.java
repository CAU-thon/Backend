package com.nuneddine.server.service;

import com.nuneddine.server.domain.Member;
import com.nuneddine.server.dto.response.UpdateUsernameResponseDto;
import com.nuneddine.server.exception.CustomException;
import com.nuneddine.server.exception.ErrorCode;
import com.nuneddine.server.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return member;
    }

    @Transactional
    public Member getMemberByKakaoId(Long kakaoId) {
        Member member = memberRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_GIVEN_KAKAO_ID));

        return member;
    }

    @Transactional
    public UpdateUsernameResponseDto updateMemberUsername(Long id, String username) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        member.updateUsername(username);
        memberRepository.save(member);

        UpdateUsernameResponseDto responseDto = new UpdateUsernameResponseDto(member.getId(), member.getUsername());
        return responseDto;
    }
}
