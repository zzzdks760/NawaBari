package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.review.Photo;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.repository.MemberRepository;
import com.backend.NawaBari.repository.RestaurantRepository;
import com.backend.NawaBari.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    /**
     * 리뷰 생성
     */
    @Transactional
    public Long createReview(Long memberId, Long restaurantId, List<Photo> photos, String title, String content, Double rate) {
        Member member = memberRepository.findOne(memberId);
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);

        Review review = Review.createReview(member, restaurant, photos, title, content, rate);
        restaurant.addReview(review);
        restaurant.updateAverageRating();
        restaurantRepository.save(restaurant);
        reviewRepository.save(review);

        return review.getId();
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public void updateReview(Long reviewId, List<Photo> photos, String title, String content, Double rate) {
        Review review = reviewRepository.findOne(reviewId);

        review.setPhotos(photos);
        review.setTitle(title);
        review.setContent(content);
        review.setRate(rate);
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public void deleteReview(Long reviewId, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        Review review = reviewRepository.findOne(reviewId);
        
        restaurant.removeReview(review);
        restaurant.updateAverageRating();
        reviewRepository.delete(review);
    }


}
