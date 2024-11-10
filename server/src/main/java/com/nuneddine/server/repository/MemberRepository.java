package com.nuneddine.server.repository;

import com.nuneddine.server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Optional<Member> findByKakaoId(Long kakaoId);
}
