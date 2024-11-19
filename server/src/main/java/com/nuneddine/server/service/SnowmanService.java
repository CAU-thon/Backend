package com.nuneddine.server.service;

import com.nuneddine.server.domain.*;
import com.nuneddine.server.dto.request.SnowmanRequestDto;
import com.nuneddine.server.dto.request.SnowmanUpdateRequestDto;
import com.nuneddine.server.dto.response.*;
import com.nuneddine.server.exception.CustomException;
import com.nuneddine.server.exception.ErrorCode;
import com.nuneddine.server.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SnowmanService {

    private final SnowmanRepository snowmanRepository;
    private final ChoiceRepository choiceRepository;
    private final MemberSnowmanRepository memberSnowmanRepository;
    private final FileUploadService fileUploadService;

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
        String base64Img = snowmanRequestDto.getImage();
        String imgUrl = fileUploadService.uploadImage(base64Img);

        Snowman snowman = Snowman.builder()
                .name(snowmanRequestDto.getName())
                .image(imgUrl)
                .mapNumber(mapNumber)
                .posX(snowmanRequestDto.getPosX())
                .posY(snowmanRequestDto.getPosY())
                .quiz(snowmanRequestDto.getQuiz())
                .answerId(snowmanRequestDto.getAnswerId())
                .member(member)
                .build();
        snowmanRepository.save(snowman);

        MemberSnowman memberSnowman = MemberSnowman.builder()
                .member(member)
                .snowman(snowman)
                .build();
        memberSnowmanRepository.save(memberSnowman);

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

        member.increaseBuild();

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
        Member member = snowman.getMember();

        SnowmanAllDetailResponseDto snowmanAllDetailResponseDto = new SnowmanAllDetailResponseDto(
                snowmanId,
                snowman.getName(),
                snowman.getImage(),
                snowman.getMapNumber(),
                snowman.getPosX(),
                snowman.getPosY(),
                member.getId(),
                snowman.getQuiz(),
                snowman.getAnswerId(),
                choices.get(0).getContent(),
                choices.get(1).getContent(),
                choices.get(2).getContent(),
                snowman.getCreatedAt(),
                snowman.getUpdatedAt()
        );

        return snowmanAllDetailResponseDto;
    }

    @Transactional
    public Long deleteSnowman(Long snowmanId, Member member) {
        Snowman snowman = snowmanRepository.findById(snowmanId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 눈사람이 없습니다."));

        // 해당 사용자가 만든 눈사람만 삭제 가능
        if (!snowman.getMember().equals(member)) {
            throw new RuntimeException("본인이 만든 눈사람이 아닙니다.");
        }

        // 눈사람 삭제
        snowmanRepository.delete(snowman);
        member.decreaseBuild();
        return snowmanId;
    }


    @Transactional
    public SnowmanQuizResponseDto findSnowmanQuiz(Long snowmanId, Member member) {
        Snowman snowman = snowmanRepository.findById(snowmanId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 눈사람이 없습니다."));
        List<Choice> choices = choiceRepository.findBySnowman(snowman);

        // 해당 멤버가 이미 푼 문제인지
        Optional<MemberSnowman> optional = memberSnowmanRepository.findByMemberAndSnowman(member, snowman);
        boolean isSolved = false;
        Long myAnswerId = 0L;
        if (optional.isPresent()) {
            // 이미 푼 문제라면
            isSolved = true;
            MemberSnowman memberSnowman = optional.get();
            myAnswerId = memberSnowman.getMyChoice();
        }

        // 헤당 멤버가 만든 문제이면 문제 맞춘 걸로 반환
        if (member.equals(snowman.getMember())) {
            isSolved = true;
            myAnswerId = snowman.getAnswerId();
        }

        // 각 선지의 비율 계산
        double countChoice1 = Double.valueOf(choices.get(0).getCount());
        double countChoice2 = Double.valueOf(choices.get(1).getCount());
        double countChoice3 = Double.valueOf(choices.get(2).getCount());
        int countAll = (int)(countChoice1 + countChoice2 + countChoice3);

        double ratioChoice1 = (countAll > 0) ? countChoice1 / countAll : 0.0;
        double ratioChoice2 = (countAll > 0) ? countChoice2 / countAll : 0.0;
        double ratioChoice3 = (countAll > 0) ? countChoice3 / countAll : 0.0;

        return new SnowmanQuizResponseDto(snowman.getId(),
                snowman.getName(), snowman.getMember().getUsername(), snowman.getImage(),
                snowman.getQuiz(), snowman.getAnswerId(),
                choices.get(0).getContent(), choices.get(1).getContent(), choices.get(2).getContent(),
                isSolved, myAnswerId,
                ratioChoice1, ratioChoice2, ratioChoice3);
    }

    @Transactional
    public Boolean solveSnowmanQuiz(Long snowmanId, Long number, Member member) {
        Snowman snowman = snowmanRepository.findById(snowmanId)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 눈사람이 없습니다."));

        // 해당 사용자가 이미 푼 퀴즈라면
        if (memberSnowmanRepository.findByMemberAndSnowman(member, snowman).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_SOLVED_QUIZ);
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
        member.decreaseChance();
        if (Objects.equals(snowman.getAnswerId(), number)) { // 사용자가 고른 답이 정답이라면
            member.increasePoint();
            return true;
        }
        else {
            return false;
        }
    }
}
