package com.nuneddine.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_snowman")
public class MemberSnowman {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "memberSnowmanId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "memberId")
    private Member member;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JoinColumn(name = "snowmanId")
//    private Snowman snowman;

    private Long myChoice;

    @Builder
    public MemberSnowman(Member member, Snowman snowman, Long myChoice) {
        this.member = member;
//        this.snowman = snowman;
        this.myChoice = myChoice;
    }
}
