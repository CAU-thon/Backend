package com.nuneddine.server.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuneddine.server.domain.*;
import com.nuneddine.server.dto.request.SnowmanItemRequest;
import com.nuneddine.server.dto.response.MemberItemResponse;
import com.nuneddine.server.repository.ItemRepository;
import com.nuneddine.server.repository.MemberItemRepository;
import com.nuneddine.server.repository.SnowmanItemRepository;
import com.nuneddine.server.repository.SnowmanRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
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

    @Value("${json.file.path}")
    private String filePath;

    // 기본제공 아이템 리스트
    private List<Item> defaultItems;

    private void loadDefaultItems(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Item> items = new ArrayList<>();
        try {
//            JsonNode rootNode = objectMapper.readTree(new File(filePath));
//            JsonNode itemsNode = rootNode.get("items");

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
    public Item gachaItem(Member member) {
        // 전체 아이템 풀
        List<Item> allItemPool = itemRepository.findAll();

        // 가챠 대상 아이템 풀
        List<Item> gachaItemPool = new ArrayList<>(allItemPool);
        gachaItemPool.removeAll(getItemsByMember(member));

        // 가챠 로직 구현 (가중치 x)
        // 가중치가 필요할까? 어차피 아이템에 등급이 없으니까 가챠 대상 아이템에 대해서는 완전 랜덤해도 되는거 아님?
        // ex. 과잠용 아이템 풀 따로 만들어서 안에서는 무차별하게
        if (!gachaItemPool.isEmpty()) {
            Random random = new Random();
            Item gachaItem = gachaItemPool.get(random.nextInt(gachaItemPool.size()));
            addItemIntoMember(member, gachaItem);
            return gachaItem;
        }

        // 뽑을 아이템이 없는 경우
        return null;
    }

    // Member가 가진 아이템을 List<MemberItem>로 받아서 List<Item>으로 반환
    // 이때, 만약 커스텀 아이템 (imgUrl != null)인 경우에는 포함하면 안됨!
    @Transactional
    public List<Item> getItemsByMember(Member member) {
        List<MemberItem> memberItems = memberItemRepository.findByMember(member);
        return memberItems.stream()
                .map(MemberItem::getItem)
                .filter(item -> item.getImgUrl() == null)
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
                    boolean isUnlocked = !memberItemIds.contains(item.getId());
                    return new MemberItemResponse(isUnlocked, item.getId());
                })
                .collect(Collectors.toList());

        return memberItemResponses;
    }
}
