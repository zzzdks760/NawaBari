package com.backend.NawaBari.service;


import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.review.Photo;
import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.repository.MemberRepository;
import com.backend.NawaBari.repository.RestaurantRepository;
import com.backend.NawaBari.repository.ReviewRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired ReviewRepository reviewRepository;
    @Autowired RestaurantRepository restaurantRepository;
    @Autowired ReviewService reviewService;
    @Autowired EntityManager em;


    @Test
    public void 리뷰생성() throws Exception {
        //given
        Member member = Member.builder()
                .profile_nickname("Kim")
                .build();
        em.persist(member);

        Restaurant restaurant = Restaurant.builder()
                .name("Vips")
                .build();
        em.persist(restaurant);

        List<Photo> photos = new ArrayList<>();
        Photo photo1 = Photo.builder()
                .photo_url("www.photo1.com")
                .build();
        em.persist(photo1);
        Photo photo2 = Photo.builder()
                .photo_url("www.photo2.com")
                .build();
        em.persist(photo2);

        photos.add(photo1);
        photos.add(photo2);

        Double rate = 4.5;
        String title = "GOOD";
        String content = "Good dish";


        //when
        Long reviewId = reviewService.createReview(member.getId(), restaurant.getId(), photos, title, content, rate);

        Review review = reviewRepository.findOne(reviewId);

        Restaurant restaurant1 = restaurantRepository.findOne(restaurant.getId());
        //then
        assertThat(review.getWriter()).isEqualTo(member);
        assertThat(review.getRestaurant()).isEqualTo(restaurant);
        assertThat(review.getPhotos()).containsExactlyInAnyOrder(photo1, photo2);
        assertThat(review.getTitle()).isEqualTo(title);
        assertThat(review.getContent()).isEqualTo(content);
        assertThat(review.getRate()).isEqualTo(rate);
        assertThat(restaurant1.getReviewCount()).isEqualTo(1);
    }

    @Test
    public void 리뷰수정() throws Exception {
        //given
        Member member = Member.builder()
                .profile_nickname("Kim")
                .build();
        em.persist(member);

        Restaurant restaurant = Restaurant.builder()
                .name("Vips")
                .build();
        em.persist(restaurant);

        List<Photo> photos = new ArrayList<>();
        Photo photo1 = Photo.builder()
                .photo_url("www.photo1.com")
                .build();
        em.persist(photo1);
        Photo photo2 = Photo.builder()
                .photo_url("www.photo2.com")
                .build();
        em.persist(photo2);

        photos.add(photo1);
        photos.add(photo2);

        Double rate = 4.5;
        String title = "GOOD";
        String content = "Good dish";

        Long reviewId = reviewService.createReview(member.getId(), restaurant.getId(), photos, title, content, rate);
        //when
        Double newRate = 3.5;
        String newTitle = "BAD";
        String newContent = "Bad dish";

        reviewService.updateReview(reviewId, photos,newTitle, newContent, newRate);

        Review updateReview = reviewRepository.findOne(reviewId);

        //then
        assertThat(updateReview.getPhotos()).isEqualTo(photos);
        assertThat(updateReview.getTitle()).isEqualTo(newTitle);
        assertThat(updateReview.getContent()).isEqualTo(newContent);
        assertThat(updateReview.getRate()).isEqualTo(newRate);
    }

    @Test
    public void 리뷰삭제() throws Exception {
        //given
        Member member = Member.builder()
                .profile_nickname("Kim")
                .build();
        em.persist(member);

        Restaurant restaurant = Restaurant.builder()
                .name("Vips")
                .build();
        em.persist(restaurant);

        List<Photo> photos = new ArrayList<>();
        Photo photo1 = Photo.builder()
                .photo_url("www.photo1.com")
                .build();
        em.persist(photo1);
        Photo photo2 = Photo.builder()
                .photo_url("www.photo2.com")
                .build();
        em.persist(photo2);

        photos.add(photo1);
        photos.add(photo2);

        Double rate = 4.5;
        String title = "GOOD";
        String content = "Good dish";

        Long reviewId = reviewService.createReview(member.getId(), restaurant.getId(), photos, title, content, rate);

        //when

        Review review = reviewRepository.findOne(reviewId);

        Restaurant restaurant1 = restaurantRepository.findOne(restaurant.getId());

        restaurant.removeReview(review);

        //then

        assertThat(restaurant1.getReviewCount()).isEqualTo(0);


    }
}
