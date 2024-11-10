package com.nuneddine.server.repository;

import com.nuneddine.server.domain.Member;
import com.nuneddine.server.domain.Snowman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SnowmanRepository extends JpaRepository<Snowman, Long> {
    Snowman findById(int id);
    List<Snowman> findByMapNumber(int mapNumber);
    List<Snowman> findByMember(Member member);
}
