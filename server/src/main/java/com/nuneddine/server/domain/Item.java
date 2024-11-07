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
    private String imgUrl; // custom item용 imgUrl, default=null
    private ItemCategory itemCategory;

    @Builder
    public Item(String itemName, String imgUrl,ItemCategory itemCategory) {
        this.itemName = itemName;
        this.imgUrl = imgUrl;
        this.itemCategory = itemCategory;
    }
}
