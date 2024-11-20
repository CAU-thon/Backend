package com.nuneddine.server.util;

import com.nuneddine.server.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ResetFieldScheduler {

    private final MemberRepository memberRepository;

    //매일 자정에 실행 (초 분 시 일 월 요일)
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void resetFields() {
        memberRepository.resetFieldsToDefault(10);
    }
}
