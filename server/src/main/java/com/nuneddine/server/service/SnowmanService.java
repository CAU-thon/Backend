package com.nuneddine.server.service;

import com.nuneddine.server.domain.Snowman;
import com.nuneddine.server.dto.response.SnowmanResponseDto;
import com.nuneddine.server.repository.SnowmanRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SnowmanService {
    @Autowired
    SnowmanRepository snowmanRepository;

    @Transactional
    public List<SnowmanResponseDto> findSnowmansByMap(int mapNumber) {
        List<Snowman> snowmans = snowmanRepository.findByMapNumber(mapNumber);

        // 눈사람의 id, 이미지, 위치를 반환
        return snowmans.stream()
                .map(snowman -> new SnowmanResponseDto(snowman.getId(), snowman.getImage(), snowman.getPosX(), snowman.getPosY()))
                .collect(Collectors.toList());
    }
}
