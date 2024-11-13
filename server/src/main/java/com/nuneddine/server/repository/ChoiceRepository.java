package com.nuneddine.server.repository;

import com.nuneddine.server.domain.Choice;
import com.nuneddine.server.domain.Snowman;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {
    List<Choice> findBySnowman(Snowman snowman);
}
