package com.nuneddine.server.repository;

import com.nuneddine.server.domain.Snowman;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnowmanRepository extends JpaRepository<Snowman, Long> {
}
