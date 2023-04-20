package com.backend.NawaBari.domain.review;

import com.backend.NawaBari.domain.Base;
import com.backend.NawaBari.domain.Like;
import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.dto.ReviewDTO;
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

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "review")
    private List<Like> likes = new ArrayList<>();


    //== 연관관계 메서드 ==//
    public void setMember(Member member) {
        this.writer = member;
        member.getReviews().add(this);
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
        photo.setReview(this);
    }

    public void addLike(Like like) {
        this.likes.add(like);
        like.setReview(this);
    }

    public void removeLike(Like like) {
        this.likes.remove(like);
        like.setReview(null);
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


}

