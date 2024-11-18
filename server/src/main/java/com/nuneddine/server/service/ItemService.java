package com.nuneddine.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuneddine.server.domain.*;
import com.nuneddine.server.dto.request.SnowmanItemRequest;
import com.nuneddine.server.dto.response.GachaResponseDto;
import com.nuneddine.server.dto.response.MemberItemResponse;
import com.nuneddine.server.exception.CustomException;
import com.nuneddine.server.exception.ErrorCode;
import com.nuneddine.server.repository.ItemRepository;
import com.nuneddine.server.repository.MemberItemRepository;
import com.nuneddine.server.repository.SnowmanItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MemberItemRepository memberItemRepository;
    @Autowired
    private SnowmanItemRepository snowmanItemRepository;

    // local에서 사용할 filePath
    // push 할 때는 이 파일 경로를 주석 처리
//    @Value("${json.file.path}")
//    private String filePath;
    // ec2에서 사용할 filePath
    private String filePath = System.getProperty("user.home") + "/Backend/server/src/main/resources/defaultItems.json";

    // 기본제공 아이템 리스트
    private List<Item> defaultItems;

    @Transactional
    private void loadDefaultItems(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Item> items = new ArrayList<>();
        try {
            // 파일 내용을 String으로 읽기
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // String에서 JsonNode를 생성하기
            JsonNode rootNode = objectMapper.readTree(jsonContent);
            JsonNode itemsNode = rootNode.get("items");

            if (itemsNode != null && itemsNode.isArray()) {
                for (JsonNode itemNode : itemsNode) {
                    Long id = itemNode.get("id").asLong();
                    Item item = itemRepository.findById(id).orElse(null);
                    if (item != null) {
                        items.add(item);
                    }
                }
            }
            defaultItems = items;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void setDefaultItems(Member member) {
        if (defaultItems == null) {
            loadDefaultItems(filePath);
        }
        for(Item item : defaultItems) {
            addItemIntoMember(member, item);
        }
    }

    // 아이템 가챠(모든 아이템 기준)
    @Transactional
    public GachaResponseDto gachaItem(Member member) {
        if (canGacha(member)) {
            // 가챠 가능
            Item item = gacha(member);
            return new GachaResponseDto(item, true);
        }
        else {
            // 가챠 불가능
            return new GachaResponseDto(null, false);
        }
    }

    @Transactional
    private Item gacha(Member member) {
        // 전체 아이템 풀
        List<Item> allItemPool = itemRepository.findAll();

        // 가챠 대상 아이템 풀
        List<Item> gachaItemPool = new ArrayList<>(allItemPool);
        gachaItemPool.removeAll(getItemsByMember(member));

        // 가챠 로직 구현 (가중치 x)
        // 가중치가 필요할까? 어차피 아이템에 등급이 없으니까 가챠 대상 아이템에 대해서는 완전 랜덤해도 되는거 아님?
        // ex. 과잠용 아이템 풀 따로 만들어서 안에서는 무차별하게
        if (!gachaItemPool.isEmpty()) {
            // 꽝의 비율 설정
            double failRate = 0.3; // 30% 꽝 확률
            Random random = new Random();

            if (!gachaItemPool.isEmpty()) {
                // 랜덤 값으로 꽝을 결정
                if (random.nextDouble() < failRate) {
                    // 꽝 처리
                    throw new CustomException(ErrorCode.FAILED_TO_GACHA);
                }
            }
                Item gachaItem = gachaItemPool.get(random.nextInt(gachaItemPool.size()));
                addItemIntoMember(member, gachaItem);
                member.decreasePoint();
                return gachaItem;
        }
        // 뽑을 아이템이 없는 경우
        return null;
    }

    // 가챠 가능 여부
    @Transactional
    private boolean canGacha(Member member) {
        if ( member.getPoint() > 300 ) {
            return true;
        } else return false;
    }

    // Member가 가진 아이템을 List<MemberItem>로 받아서 List<Item>으로 반환
    @Transactional
    public List<Item> getItemsByMember(Member member) {
        List<MemberItem> memberItems = memberItemRepository.findByMember(member);
        return memberItems.stream()
                .map(MemberItem::getItem)
                .collect(Collectors.toList());
    }

    // 특정 Snowman의 SnowmanItem 모두 호출
    @Transactional
    public List<SnowmanItem> getItemsBySnowman(Snowman snowman) {
        List<SnowmanItem> snowmanItems = snowmanItemRepository.findBySnowman(snowman);
        return snowmanItems;
    }

    // Member 에 Item 추가
    @Transactional
    public void addItemIntoMember(Member member, Item item) {
        MemberItem newMemberItem = MemberItem.builder()
                .item(item)
                .member(member)
                .build();
        memberItemRepository.save(newMemberItem);
    }

    // SnowmanItem 에 Item 추가
    @Transactional
    public void addItemIntoSnowman(Snowman snowman, SnowmanItemRequest request) {
        SnowmanItem newSnowManItem = SnowmanItem.builder()
                .item(itemRepository.getItemById(request.getId()))
                .build();
        snowmanItemRepository.save(newSnowManItem);
    }

    // SnowmanItem 삭제
    @Transactional
    public void deleteSnowmanItem(SnowmanItemRequest request) {
        snowmanItemRepository.deleteSnowmanItemByItem(itemRepository.getItemById(request.getId()));
    }

    // SnowmanItem 수정
    @Transactional
    public SnowmanItem updateSnowmanItem(SnowmanItemRequest request) {
        SnowmanItem snowmanItem = snowmanItemRepository.findByItem(itemRepository.getItemById(request.getId()))
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 아이템이 없습니다."));

        snowmanItemRepository.save(snowmanItem);
        return snowmanItem;
    }

    // mypage에서 본인의 아이템 인벤토리 확인
    @Transactional
    public List<MemberItemResponse> getAllMemberItem(Member member) {
        List<Item> allItems = itemRepository.findAll();
        List<Item> memberItems = getItemsByMember(member);

        // memberItems에서 ItemId list 만들기
        Set<Long> memberItemIds = memberItems.stream()
                .map(Item::getId)
                .collect(Collectors.toSet());

        // allItems를 순회하면서 없으면 isUnlock = true로 설정
        List<MemberItemResponse> memberItemResponses = allItems.stream()
                .map(item -> {
                    boolean available = memberItemIds.contains(item.getId());
                    return new MemberItemResponse(available, item.getId(), item.getItemName());
                })
                .collect(Collectors.toList());

        return memberItemResponses;
    }
}
