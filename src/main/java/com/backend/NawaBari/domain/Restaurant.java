package com.backend.NawaBari.domain;

import com.backend.NawaBari.domain.category.Category;
import com.backend.NawaBari.domain.review.Review;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter@Setter
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


    //== 연관관계 메서드 ==//
    public void setZone(Zone zone) {
        this.zone = zone;
        zone.getRestaurants().add(this);
    }

    public void setCategory(Category category) {
        this.category = category;
        category.getRestaurants().add(this);
    }

    @Builder
    public Restaurant(Long id, String name) {
        this.id = id;
        this.name = name;

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
