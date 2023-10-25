package com.backend.NawaBari.domain;

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

    private String address_name;

    private String tel;

    private String opening_hours;

    private String holidays;

    private int reviewCount = 0;

    private Double avgRating = 0.0;

    private String main_photo_url;

    private int bookMarkCount = 0;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE)
    private List<BookMark> bookMarks = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Menu> menus = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;


    //== 연관관계 메서드 ==//
    public void setZone(Zone zone) {
        this.zone = zone;
        zone.getRestaurants().add(this);
    }


    @Builder
    public Restaurant(String name, String address_name, String tel, String opening_hours, String holidays, int reviewCount, Double avgRating, String main_photo_url, List<Review> reviews, List<Menu> menus, int bookMarkCount, Zone zone) {
        this.name = name;
        this.address_name = address_name;
        this.tel = tel;
        this.opening_hours = opening_hours;
        this.holidays = holidays;
        this.reviewCount = reviewCount;
        this.avgRating = avgRating;
        this.main_photo_url = main_photo_url;
        this.reviews = reviews;
        this.menus = menus;
        this.bookMarkCount = bookMarkCount;
        this.zone = zone;
    }


    //북마크 추가될 때 전체 북마크 수 증가
    public void addBookMark(BookMark bookMark) {
        this.bookMarks.add(bookMark);
        bookMark.setRestaurant(this);
        this.bookMarkCount++;
    }

    //북마크 삭제될 때 전체 북마크 수 감소
    public void removeBookMark(BookMark bookMark) {
        this.bookMarks.remove(bookMark);
        bookMark.setRestaurant(null);
        this.bookMarkCount--;
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
