package com.nuneddine.server.service;

import com.nuneddine.server.domain.*;
import com.nuneddine.server.dto.request.SnowmanItemRequest;
import com.nuneddine.server.repository.ItemRepository;
import com.nuneddine.server.repository.MemberItemRepository;
import com.nuneddine.server.repository.SnowmanItemRepository;
import com.nuneddine.server.repository.SnowmanRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MemberItemRepository memberItemRepository;
    @Autowired
    private SnowmanItemRepository snowmanItemRepository;

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
            return gachaItemPool.get(random.nextInt(gachaItemPool.size()));
        }

        // 뽑을 아이템이 없는 경우
        return null;
    }

    // Member가 가진 아이템을 List<MemberItem>로 받아서 List<Item>으로 반환
    @Transactional
    private List<Item> getItemsByMember(Member member) {
        List<MemberItem> memberItems = memberItemRepository.findByMember(member);
        return memberItems.stream()
                .map(MemberItem::getItem)
                .collect(Collectors.toList());
    }

    // 특정 Snowman의 SnowmanItem 모두 호출
    @Transactional
    private List<SnowmanItem> getItemsBySnowman(Snowman snowman) {
        List<SnowmanItem> snowmanItems = snowmanItemRepository.findBySnowman(snowman);
        return snowmanItems;
    }

    // SnowmanItem 에 Item 추가
    @Transactional
    private void addItemIntoSnowman(Snowman snowman, SnowmanItemRequest request) {
        SnowmanItem newSnowManItem = SnowmanItem.builder()
                .posX(request.getPosX())
                .posY(request.getPosY())
                .posZ(request.getPosZ())
                .item(itemRepository.getItemById(request.getId()))
                .build();
        snowmanItemRepository.save(newSnowManItem);
    }

    // SnowmanItem 삭제
    @Transactional
    private void deleteSnowmanItem(SnowmanItemRequest request) {
        snowmanItemRepository.deleteSnowmanItemByItem(itemRepository.getItemById(request.getId()));
    }

    // SnowmanItem 수정
    @Transactional
    private SnowmanItem updateSnowmanItem(SnowmanItemRequest request) {
        SnowmanItem snowmanItem = snowmanItemRepository.findByItem(itemRepository.getItemById(request.getId()))
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 아이템이 없습니다."));

        snowmanItem.setPosX(request.getPosX());
        snowmanItem.setPosY(request.getPosY());
        snowmanItem.setPosZ(request.getPosZ());

        snowmanItemRepository.save(snowmanItem);
        return snowmanItem;
    }
}
