package com.nuneddine.server.repository;

import com.nuneddine.server.domain.Item;
import com.nuneddine.server.domain.Snowman;
import com.nuneddine.server.domain.SnowmanItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface SnowmanItemRepository extends JpaRepository<SnowmanItem, Long> {
    List<SnowmanItem> findBySnowman(Snowman snowman);
    Optional<SnowmanItem> findByItem(Item item);
    void deleteSnowmanItemByItem(Item item);
}
