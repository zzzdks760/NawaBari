package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.MemberZone;
import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.Zone;
import com.backend.NawaBari.domain.review.Photo;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.repository.MemberRepository;
import com.backend.NawaBari.repository.RestaurantRepository;
import com.backend.NawaBari.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
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

        boolean isAddressMatching = false;
        for (MemberZone memberZone : member.getMemberZones()) {
            if (checkAddress(memberZone.getZone(), restaurant.getAddress_name())) {
                isAddressMatching = true;
                break;
            }
        }

        if (isAddressMatching) { //구 주소가 일치하는 경우 리뷰생성
            Review review = Review.createReview(member, restaurant, photos, title, content, rate);
            restaurant.addReview(review);
            restaurant.updateAverageRating();
            restaurantRepository.save(restaurant);
            reviewRepository.save(review);

            return review.getId();
        } else {
            throw new IllegalArgumentException("리뷰를 작성할 수 있는 구역이 아닙니다.");
        }

    }

    //추출한 구 이름과 식당주소가 일치하는지 판별
    private boolean checkAddress(Zone zone, String address_name) {
        String extractedDistrict = extractDistrictFromAddress(zone);
        return address_name.contains(extractedDistrict);
    }

    //구역의 구이름 추출
    private String extractDistrictFromAddress(Zone zone) {
       return zone.getGu();
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    public void updateReview(Long reviewId, Long restaurantId, List<Photo> photos, String title, String content, Double rate) {
        Review review = reviewRepository.findOne(reviewId);
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);

        review.getPhotos().clear();

        for (Photo photo : photos) {
            review.getPhotos().add(photo);
        }

        review.setTitle(title);
        review.setContent(content);
        review.setRate(rate);

        restaurant.setAvgRating(restaurant.getAvgRating());
        restaurant.updateAverageRating();
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    public Boolean deleteReview(Long reviewId, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findOne(restaurantId);
        Review review = reviewRepository.findOne(reviewId);

        if (review == null) {
            return false;
        }

        restaurant.removeReview(review);
        restaurant.updateAverageRating();
        reviewRepository.delete(review);

        return true;
    }

    /**
     * 리뷰 전체 조회
     */
    public Slice<Review> findAllReview(Long restaurantId, Pageable pageable) {
        Slice<Review> reviewList = reviewRepository.findAllReview(restaurantId, pageable);

        return reviewList;
    }

    /**
     * 리뷰 단건 조회
     */
    public Review findOne(Long id) {
        return reviewRepository.findOne(id);
    }

    /**
     * 특정 회원 리뷰 조회
     */
    public Slice<Review> findMyReview(Long memberId, Pageable pageable) {
        Slice<Review> reviewsByMember = reviewRepository.getReviewsByMember(memberId, pageable);

        return reviewsByMember;
    }
}
