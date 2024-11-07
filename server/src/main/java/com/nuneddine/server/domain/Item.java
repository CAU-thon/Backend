package com.nuneddine.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemId")
    private Long id;

    @Column(unique = true) // 일단 unique로 해둠
    private String itemName;
    private ItemCategory itemCategory;

    @Builder
    public Item(String itemName, ItemCategory itemCategory) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
    }
}
