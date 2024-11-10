package com.nuneddine.server.repository;

import com.nuneddine.server.domain.Snowman;
import com.nuneddine.server.domain.SnowmanItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SnowmanItemRepository extends JpaRepository<SnowmanItem, Long> {
    List<SnowmanItem> findBySnowman(Snowman snowman);
}
