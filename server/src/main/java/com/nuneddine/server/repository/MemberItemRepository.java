package com.nuneddine.server.repository;

import com.nuneddine.server.domain.Member;
import com.nuneddine.server.domain.MemberItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberItemRepository extends JpaRepository<MemberItem, Long> {
    List<MemberItem> findByMember(Member member);
}