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
                .memberZones(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();
        em.persist(member);
        Zone zone = Zone.builder()
                .gu("서초구")
                .dong("양재동")
                .build();
        em.persist(zone);

        MemberZone memberZone = new MemberZone(member, zone);
        member.getMemberZones().add(memberZone);

        Member member2 = Member.builder()
                .profile_nickname("Nam")
                .build();
        em.persist(member);
        em.persist(member2);

        Restaurant restaurant = Restaurant.builder()
                .name("Vips")
                .address_name("서울특별시 강남구 논현동")
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

        Double rate2 = 3.5;
        String title2 = "SoSo";
        String content2 = "SoSo dish";



        //when
        Long reviewId1 = reviewService.createReview(member.getId(), restaurant.getId(), photos, title, content, rate);
        Long reviewId2 = reviewService.createReview(member2.getId(), restaurant.getId(), photos, title2, content2, rate2);

        Review review = reviewRepository.findOne(reviewId1);

        Restaurant restaurant1 = restaurantRepository.findOne(restaurant.getId());
        //then
        assertThat(review.getWriter()).isEqualTo(member);
        assertThat(review.getRestaurant()).isEqualTo(restaurant);
        assertThat(review.getPhotos()).containsExactlyInAnyOrder(photo1, photo2);
        assertThat(review.getTitle()).isEqualTo(title);
        assertThat(review.getContent()).isEqualTo(content);
        assertThat(review.getRate()).isEqualTo(rate);
        assertThat(restaurant1.getReviewCount()).isEqualTo(2);
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

        reviewService.deleteReview(reviewId, restaurant.getId());

        assertThat(restaurant.getReviewCount()).isEqualTo(0);
        assertThat(restaurant.getAvgRating()).isNull();

    }

    @Test
    public void 리뷰전체조회() throws Exception {
        //given
        Restaurant restaurant = Restaurant.builder()
                .name("Vips")
                .reviews(new ArrayList<>())
                .build();
        em.persist(restaurant);

        Review review1 = Review.builder()
                .title("review1")
                .likeCount(10)
                .build();
        Review review2 = Review.builder()
                .title("review2")
                .likeCount(11)
                .build();
        Review review3 = Review.builder()
                .title("review3")
                .likeCount(1)
                .build();
        Review review4 = Review.builder()
                .title("review4")
                .likeCount(5)
                .build();

        restaurant.addReview(review1);
        restaurant.addReview(review2);
        restaurant.addReview(review3);
        restaurant.addReview(review4);
        em.persist(restaurant);
        em.persist(review1);
        em.persist(review2);
        em.persist(review3);
        em.persist(review4);
        em.flush();
        //when

//        List<Review> allReview = reviewRepository.findAllReview(restaurant.getId());

        //then
//        assertThat(allReview.size()).isEqualTo(4);
    }
}
