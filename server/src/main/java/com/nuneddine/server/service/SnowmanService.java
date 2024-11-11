package com.nuneddine.server.service;

import com.nuneddine.server.domain.Choice;
import com.nuneddine.server.domain.Member;
import com.nuneddine.server.domain.Snowman;
import com.nuneddine.server.dto.request.KakaoOAuthRequestDto;
import com.nuneddine.server.dto.request.SnowmanRequestDto;
import com.nuneddine.server.dto.response.SnowmanResponseDto;
import com.nuneddine.server.repository.ChoiceRepository;
import com.nuneddine.server.repository.SnowmanRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SnowmanService {
    @Autowired
    SnowmanRepository snowmanRepository;
    @Autowired
    ChoiceRepository choiceRepository;

    @Transactional
    public List<SnowmanResponseDto> findSnowmansByMap(int mapNumber) {
        List<Snowman> snowmans = snowmanRepository.findByMapNumber(mapNumber);

        // 눈사람의 id, 이미지, 위치를 반환
        return snowmans.stream()
                .map(snowman -> new SnowmanResponseDto(snowman.getId(), snowman.getImage(), snowman.getPosX(), snowman.getPosY()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createSnowman(SnowmanRequestDto snowmanRequestDto, int mapNumber, Member member) {
        Snowman snowman = Snowman.builder()
                .color(snowmanRequestDto.getColor())
                .snowmanShape(snowmanRequestDto.getSnowmanShape())
                .image(snowmanRequestDto.getImage())
                .mapNumber(mapNumber)
                .posX(snowmanRequestDto.getPosX())
                .posY(snowmanRequestDto.getPosY())
                .quiz(snowmanRequestDto.getQuiz())
                .answerId(snowmanRequestDto.getAnswerId())
                .member(member)
                .build();
        snowmanRepository.save(snowman);

        Choice choice1 = Choice.builder()
                .snowman(snowman)
                .content(snowmanRequestDto.getContent1())
                .count(0)
                .build();
        choiceRepository.save(choice1);

        Choice choice2 = Choice.builder()
                .snowman(snowman)
                .content(snowmanRequestDto.getContent2())
                .count(0)
                .build();
        choiceRepository.save(choice2);

        Choice choice3 = Choice.builder()
                .snowman(snowman)
                .content(snowmanRequestDto.getContent3())
                .count(0)
                .build();
        choiceRepository.save(choice3);

        return snowman.getId();
    }
}
