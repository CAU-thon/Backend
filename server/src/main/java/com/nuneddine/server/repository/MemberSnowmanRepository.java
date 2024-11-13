package com.nuneddine.server.repository;

import com.nuneddine.server.domain.Member;
import com.nuneddine.server.domain.MemberSnowman;
import com.nuneddine.server.domain.Snowman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberSnowmanRepository extends JpaRepository<MemberSnowman, Long> {
    Optional<MemberSnowman> findByMemberAndSnowman(Member member, Snowman snowman);
}
