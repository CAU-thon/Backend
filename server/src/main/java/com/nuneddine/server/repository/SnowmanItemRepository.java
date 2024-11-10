package com.nuneddine.server.repository;

import com.nuneddine.server.domain.SnowmanItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnowmanItemRepository extends JpaRepository<SnowmanItem, Long> {
}
