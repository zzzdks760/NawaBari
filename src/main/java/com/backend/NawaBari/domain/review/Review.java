package com.backend.NawaBari.domain.review;

import com.backend.NawaBari.domain.Base;
import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.Zone;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String title;

    private String content;

    private Double rate;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Builder
    public Review(String title, String content, Double rate, List<Photo> photos, Member writer, Zone zone, Restaurant restaurant) {
        this.title = title;
        this.content = content;
        this.rate = rate;
        this.photos = photos;
        this.writer = writer;
        this.zone = zone;
        this.restaurant = restaurant;
    }
}

