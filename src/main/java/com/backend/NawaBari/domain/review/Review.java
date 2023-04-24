package com.backend.NawaBari.domain.review;

import com.backend.NawaBari.domain.Base;
import com.backend.NawaBari.domain.Heart;
import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Restaurant;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String title;

    private String content;

    private Double rate;

    private int likeCount;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "review")
    private List<Heart> hearts = new ArrayList<>();


    //== 연관관계 메서드 ==//
    public void setMember(Member member) {
        this.writer = member;
        member.getReviews().add(this);
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
        photo.setReview(this);
    }

    public void addHeart(Heart heart) {
        this.hearts.add(heart);
        heart.setReview(this);
        this.likeCount++;
    }

    public void removeHeart(Heart heart) {
        this.hearts.remove(heart);
//        heart.setReview(null);
        this.likeCount--;
        if (this.likeCount < 0) {
            this.likeCount = 0;
        }
    }


    /**
     * 리뷰 생성
     */
    //== 생성 메서드 ==//
    public static Review createReview(Member writer, Restaurant restaurant, List<Photo> photos, String title, String content, Double rate) {
        Review review = new Review();
        review.setMember(writer);
        review.setRestaurant(restaurant);
        for (Photo photo : photos) {
            review.addPhoto(photo);
        }
        review.setTitle(title);
        review.setContent(content);
        review.setRate(rate);



        return review;
    }

    @Builder
    public Review(String title) {
        this.title = title;
    }
}

