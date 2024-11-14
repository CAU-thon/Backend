package com.nuneddine.server.service;

import com.nuneddine.server.domain.*;
import com.nuneddine.server.dto.request.KakaoOAuthRequestDto;
import com.nuneddine.server.dto.request.SnowmanRequestDto;
import com.nuneddine.server.dto.request.SnowmanUpdateRequestDto;
import com.nuneddine.server.dto.response.*;
import com.nuneddine.server.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SnowmanService {
    @Autowired
    SnowmanRepository snowmanRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ChoiceRepository choiceRepository;
    @Autowired
    MemberSnowmanRepository memberSnowmanRepository;
    @Autowired
    SnowmanItemRepository snowmanItemRepository;

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
                .name(snowmanRequestDto.getName())
                .image(snowmanRequestDto.getImage())
                .mapNumber(mapNumber)
                .posX(snowmanRequestDto.getPosX())
                .posY(snowmanRequestDto.getPosY())
                .quiz(snowmanRequestDto.getQuiz())
                .answerId(snowmanRequestDto.getAnswerId())
                .member(member)
                .build();
        snowmanRepository.save(snowman);

//        List<Long> itemIds = snowmanRequestDto.getItemIds();
//
//        for (Long itemId : itemIds) {
//            SnowmanItem snowmanItem = SnowmanItem.builder()
//                    .snowman(snowman)
//                    .item(itemRepository.getItemById(itemId))
//                    .build();
//            snowmanItemRepository.save(snowmanItem);
//        }

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

    @Transactional
    public List<SnowmanDetailResponseDto> findMySnowman(Member member) {
        List<Snowman> snowmans = snowmanRepository.findByMember(member);

        List<SnowmanDetailResponseDto> snowmanDetailResponseDtos = new ArrayList<>();
        for (Snowman snowman : snowmans) {
            int correctCount = 0; // 맞춘 사람
            int incorrectCount = 0; // 틀린 사람
            List<Choice> choices = choiceRepository.findBySnowman(snowman);

            for (int i = 0; i < 3; i++) {
                if (i == snowman.getAnswerId()-1) {
                    correctCount += choices.get(i).getCount();
                }
                else {
                    incorrectCount += choices.get(i).getCount();
                }
            }

            SnowmanDetailResponseDto dto = new SnowmanDetailResponseDto(snowman.getId(), snowman.getName(), snowman.getImage(), correctCount, incorrectCount);
            snowmanDetailResponseDtos.add(dto);
        }

        return snowmanDetailResponseDtos;
    }

    @Transactional
    public SnowmanAllDetailResponseDto allDetailSnowman(Long snowmanId) {
        Snowman snowman = snowmanRepository.findById(snowmanId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 눈사람이 없습니다."));
        List<Choice> choices = choiceRepository.findBySnowman(snowman);
        List<SnowmanItemResponseDto> snowmanItems = snowmanItemRepository.findBySnowman(snowman)
                .stream()
                .map(item -> new SnowmanItemResponseDto(item.getId()))
                .collect(Collectors.toList());
        Member member = snowman.getMember();

        SnowmanAllDetailResponseDto snowmanAllDetailResponseDto = new SnowmanAllDetailResponseDto(
                snowmanId, snowman.getName(),
                snowman.getImage(), snowman.getMapNumber(),
                snowman.getPosX(), snowman.getPosY(),
                member.getId(),
                snowman.getQuiz(), snowman.getAnswerId(),
                choices.get(0).getContent(), choices.get(1).getContent(), choices.get(2).getContent(),
                snowman.getCreatedAt(), snowman.getUpdatedAt()
        );

        return snowmanAllDetailResponseDto;
    }

    @Transactional
    public Long updateSnowman(Long snowmanId, SnowmanUpdateRequestDto requestDto, Member member) {
        Snowman snowman = snowmanRepository.findById(snowmanId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 눈사람이 없습니다."));

        // 해당 사용자가 만든 눈사람만 수정 가능
        if (snowman.getMember() != member) {
            throw new RuntimeException("본인이 만든 눈사람이 아닙니다.");
        }

        snowman.updateSnowman(requestDto.getName(), requestDto.getColor(), requestDto.getSnowmanShape(), requestDto.getImage());

        // 원래 가지고 있던 아이템들 삭제 후 재생성
        List<SnowmanItem> beforeItems = snowmanItemRepository.findBySnowman(snowman);
        for (SnowmanItem item : beforeItems) {
            snowmanItemRepository.delete(item);
        }

        List<SnowmanItem> afterItems = requestDto.getSnowmanItemRequests()
                .stream()
                .map(item -> new SnowmanItem(snowman, itemRepository.getItemById(item.getId())))
                .collect(Collectors.toList());
        for (SnowmanItem snowmanItem : afterItems) {
            snowmanItemRepository.save(snowmanItem);
        }

        return snowman.getId();
    }

    @Transactional
    public SnowmanQuizResponseDto findSnowmanQuiz(Long snowmanId) {
        Snowman snowman = snowmanRepository.findById(snowmanId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 눈사람이 없습니다."));
        List<Choice> choices = choiceRepository.findBySnowman(snowman);

        return new SnowmanQuizResponseDto(snowman.getId(), snowman.getName(), snowman.getImage(), snowman.getQuiz(), snowman.getAnswerId(), choices.get(0).getContent(), choices.get(1).getContent(), choices.get(2).getContent());
    }

    @Transactional
    public Boolean solveSnowmanQuiz(Long snowmanId, Long number, Member member) {
        Snowman snowman = snowmanRepository.findById(snowmanId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 눈사람이 없습니다."));

        // 해당 사용자가 이미 푼 퀴즈라면
        if (memberSnowmanRepository.findByMemberAndSnowman(member, snowman).isPresent()) {
            throw new IllegalStateException("이미 퀴즈를 푼 기록이 있습니다.");
        }

        // 새로운 member snowman 추가
        MemberSnowman memberSnowman = MemberSnowman.builder()
                .member(member)
                .snowman(snowman)
                .myChoice(number)
                .build();
        memberSnowmanRepository.save(memberSnowman);

        List<Choice> choices = choiceRepository.findBySnowman(snowman);

        choices.get(number.intValue()-1).countUp(); // 사용자가 고른 답안의 count 수를 1 올림
        if (Objects.equals(snowman.getAnswerId(), number)) { // 사용자가 고른 답이 정답이라면
            return true;
        }
        else {
            return false;
        }
    }
}
