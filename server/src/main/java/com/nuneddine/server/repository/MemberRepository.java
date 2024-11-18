package com.nuneddine.server.repository;

import com.nuneddine.server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Optional<Member> findByKakaoId(Long kakaoId);

    @Modifying
    @Query("UPDATE Member m SET m.chance = :defaultValue")
    public void resetFieldsToDefault(int defaultValue);
}
