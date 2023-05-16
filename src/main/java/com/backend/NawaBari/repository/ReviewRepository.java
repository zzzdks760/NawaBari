package com.backend.NawaBari.repository;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.review.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewRepository {

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    //리뷰 저장
    public void save(Review review) {
        em.persist(review);
    }

    //아이디로 리뷰조회
    public Review findOne(Long id) {
        return em.find(Review.class, id);
    }

    //리뷰 삭제
    @Transactional
    public void delete(Review review) {
        em.remove(review);
    }

    //리뷰 수 조회
    public Long countByReview(Long restaurant_id) {
        return em.createQuery("select count(r) from Review r where r.restaurant.id = :restaurant_id", Long.class)
                .setParameter("restaurant_id", restaurant_id)
                .getSingleResult();
    }

    //리뷰 조회
    public List<Review> findReview(Long restaurant_id) {
        return em.createQuery("select r from Review r where r.restaurant_id = :restaurant_id", Review.class)
                .setParameter("restaurant_id", restaurant_id)
                .getResultList();
    }


    //회원이 작성한 리뷰조회
/*    public List<Review> getReviewsByMember(Member member) {
        return em.createQuery("select r from Review where r.member = :member", Review.class)
                .setParameter("member", member)
                .getResultList();
    }*/

    //식당에 대한 리뷰조회
/*
    public List<Review> getReviewsByRestaurant(Restaurant restaurant) {
        return em.createQuery("select r from Review r where r.restaurant = :restaurant", Review.class)
                .setParameter("restaurant", restaurant)
                .getResultList();
    }
*/

}


