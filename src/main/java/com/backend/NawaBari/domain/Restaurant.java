package com.backend.NawaBari.domain;

import com.backend.NawaBari.domain.category.Category;
import com.backend.NawaBari.domain.review.Review;
import jakarta.persistence.*;
import lombok.*;

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

    private String openingTime;

    private String closingTime;

    private String address_name;

    private Double lat;

    private Double lng;

    private String tel;

    private int reviewCount = 0;

    private Double avgRating = 0.0;

    private String main_photo_fileName;

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;

    // 식당 수정 메서드
    public void update(String name, String main_photo_fileName, String openingTime, String closingTime, String address_name, String tel) {
        this.setName(name);
        this.setMain_photo_fileName(main_photo_fileName);
        this.setOpeningTime(openingTime);
        this.setClosingTime(closingTime);
        this.setAddress_name(address_name);
        this.setTel(tel);
    }


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
    public Restaurant(String name, String main_photo_fileName, String address_name, Double lat, Double lng, String tel, List<Review> reviews, int reviewCount, Zone zone, Double avgRating) {
        this.name = name;
        this.main_photo_fileName = main_photo_fileName;
        this.address_name = address_name;
        this.lat = lat;
        this.lng = lng;
        this.tel = tel;
        this.reviews = reviews;
        this.reviewCount = reviewCount;
        this.zone = zone;
        this.avgRating = avgRating;
    }




    //리뷰가 추가될 때 리뷰수도 증가
    public void addReview(Review review) {
        this.reviews.add(review);
        review.setRestaurant(this);
        this.reviewCount++;
    }

    //리뷰가 삭제될 때 리뷰수 감소
    public void removeReview(Review review) {
        this.reviews.remove(review);
        review.setRestaurant(null);
        this.reviewCount--;
    }

    //메인사진 삭제
    public void removeMainPhoto() {
        this.main_photo_fileName = null;
    }


/**
     * 리뷰 평점 계산 로직
      */
    public void updateAverageRating() {
        if (reviews.isEmpty()) {
            avgRating = 0.0;
            return;
        }
        double sum = 0;
        for (Review review : reviews) {
            sum += review.getRate();
        }
        avgRating = sum / reviews.size();
    }

}
