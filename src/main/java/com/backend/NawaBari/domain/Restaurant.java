package com.backend.NawaBari.domain;

import com.backend.NawaBari.domain.category.Category;
import com.backend.NawaBari.domain.review.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends Base{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    private String name;

    private String restaurant_img;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private String location;

    private String tel;

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @Builder
    public Restaurant(String name, String restaurant_img, LocalTime openingTime, LocalTime closingTime, String location, String tel, List<Review> reviews, Category category, Zone zone) {
        this.name = name;
        this.restaurant_img = restaurant_img;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.location = location;
        this.tel = tel;
        this.reviews = reviews;
        this.category = category;
        this.zone = zone;
    }
/**
     * 리뷰 평점 계산 로직
      */
/*
    public Double getAverageRating() {
        if (reviews.isEmpty()) {
            return null;
        }
        double sum = 0;
        for (Review review : reviews) {
            sum += review.getRate();
        }
        return sum / reviews.size();

    }
*/

}
