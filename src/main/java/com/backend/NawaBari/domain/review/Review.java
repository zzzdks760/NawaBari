package com.backend.NawaBari.domain.review;

import com.backend.NawaBari.domain.*;
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


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<Photo> photos = new ArrayList<>();



    //== 연관관계 메서드 ==//
    public void setMember(Member member) {
        this.writer = member;
        member.getReviews().add(this);
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
    public static Review createReview(Member writer, Restaurant restaurant, String title, String content, Double rate, List<Photo> photos) {
        Review review = new Review();
        review.setMember(writer);
        review.setRestaurant(restaurant);
        review.setTitle(title);
        review.setContent(content);
        review.setRate(rate);

        for (Photo photo : photos) {
            photo.setReview(review);
        }

        return review;
    }

    @Builder
    public Review(String title, int likeCount) {
        this.title = title;
        this.likeCount = likeCount;
    }

    public void removePhoto(Photo photo) {
        if (photo != null) {
            photos.remove(photo);
            photo.setReview(null);
        }
    }
}

