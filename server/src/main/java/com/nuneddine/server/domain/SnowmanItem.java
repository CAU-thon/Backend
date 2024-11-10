package com.nuneddine.server.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "snowman_item")
public class SnowmanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberItemId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "snowmanId", nullable = false)
    private Snowman snowman;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId", nullable = false)
    private Item item;

    private double posX;
    private double posY;
    private double posZ;

    @Builder
    public SnowmanItem(Snowman snowman, Item item, double posX, double posY, double posZ) {
        this.snowman = snowman;
        this.item = item;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }
}
