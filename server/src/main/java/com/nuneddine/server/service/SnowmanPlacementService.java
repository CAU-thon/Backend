package com.nuneddine.server.service;

import com.nuneddine.server.domain.Member;
import com.nuneddine.server.domain.Snowman;
import com.nuneddine.server.dto.response.SnowmanResponseDto;
import com.nuneddine.server.repository.SnowmanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SnowmanPlacementService {

    private static final double MIN_X = 0;
    private static final double MAX_X = 100;
    private static final double MIN_Y = 0;
    private static final double MAX_Y = 100;
    private static final int MAX_USER_SNOWMEN = 3;

    private final SnowmanRepository snowmanRepository;

    // 주어진 맵 번호와 그리드 수, 좌표 범위를 사용하여 각 그리드에서 하나의 눈사람을 선택하는 메소드
    public List<SnowmanResponseDto> selectSnowman(int mapNumber, Member member, int SNOWMAN_MAX_NUM) {

        // 데이터베이스에서 주어진 맵 번호에 해당하는 모든 눈사람을 가져옴
        List<Snowman> allSnowmen = snowmanRepository.findByMapNumber(mapNumber);
        if (allSnowmen.isEmpty()) {
            throw new IllegalArgumentException("해당 맵 번호에 대한 눈사람이 없습니다.");
        }

        // 본인의 눈사람 필터링 및 추가
        List<Snowman> userSnowmen = allSnowmen.stream()
                .filter(s -> s.getMember().equals(member))
                .limit(MAX_USER_SNOWMEN)
                .toList();

        // 선택된 눈사람에 본인 눈사람 우선 추가
        List<Snowman> selectedSnowmen = new ArrayList<>(userSnowmen);
        allSnowmen.removeAll(selectedSnowmen);
        Random random = new Random();

        // 각 그리드의 너비와 높이를 계산
        double gridWidth = (MAX_X - MIN_X) / SNOWMAN_MAX_NUM;
        double gridHeight = (MAX_Y - MIN_Y) / SNOWMAN_MAX_NUM;

        // 각 그리드에서 하나의 눈사람을 선택
        for (int i = 0; i < SNOWMAN_MAX_NUM; i++) {
            for (int j = 0; j < SNOWMAN_MAX_NUM; j++) {
                if (selectedSnowmen.size() >= SNOWMAN_MAX_NUM) {
                    break;
                }

                // 해당 그리드에 속하는 눈사람을 필터링
                double gridStartX = MIN_X + i * gridWidth;
                double gridEndX = gridStartX + gridWidth;
                double gridStartY = MIN_Y + j * gridHeight;
                double gridEndY = gridStartY + gridHeight;

                List<Snowman> gridsnowman = allSnowmen.stream()
                        .filter(s -> s.getPosX() >= gridStartX && s.getPosX() < gridEndX
                                && s.getPosY() >= gridStartY && s.getPosY() < gridEndY)
                        .toList();

                if (!gridsnowman.isEmpty()) {
                    // 랜덤하게 그리드 내의 눈사람 선택
                    Snowman selectedSnowman = gridsnowman.get(random.nextInt(gridsnowman.size()));
                    selectedSnowmen.add(selectedSnowman);
                }
            }
        }

         // SNOWMAN_MAX_NUM 개의 눈사람을 보장하기 위해 부족할 경우 추가 선택
        while (selectedSnowmen.size() < SNOWMAN_MAX_NUM && selectedSnowmen.size() < allSnowmen.size()) {
            List<Snowman> unselectedSnowmen = new ArrayList<>(allSnowmen);
            unselectedSnowmen.removeAll(selectedSnowmen);

            if (!unselectedSnowmen.isEmpty()) {
                Snowman additionalSnowman = unselectedSnowmen.get(random.nextInt(unselectedSnowmen.size()));
                selectedSnowmen.add(additionalSnowman);
            }
        }

        // SnowmanResponseDto 리스트로 반환
        List<SnowmanResponseDto> response = new ArrayList<>();
        for (Snowman snowman : selectedSnowmen) {
            response.add(new SnowmanResponseDto(snowman.getId(), snowman.getImage(), snowman.getPosX(), snowman.getPosY()));
        }

        return response;
    }

    public List<SnowmanResponseDto> selectAll(int mapNumber) {
        List<Snowman> snowmans = snowmanRepository.findByMapNumber(mapNumber);

        List<SnowmanResponseDto> response = new ArrayList<>();
        for (Snowman snowman : snowmans) {
            response.add(new SnowmanResponseDto(snowman.getId(), snowman.getImage(), snowman.getPosX(), snowman.getPosY()));
        }
        return response;
    }
}
