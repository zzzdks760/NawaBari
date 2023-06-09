package com.backend.NawaBari.repository;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.review.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantRepository {

    @PersistenceContext
    private final EntityManager em;

    //레스토랑 등록
    @Transactional
    public void save(Restaurant restaurant) {
        em.persist(restaurant);
    }

    public Restaurant findOne(Long restaurant_id) {
        return em.find(Restaurant.class, restaurant_id);
    }

    //식당 상세조회
    public List<Review> findReviewTop3(Long restaurant_id) {
        return em.createQuery("select r from Review r where r.restaurant.id = :restaurant_id ORDER BY likeCount DESC", Review.class)
                .setParameter("restaurant_id", restaurant_id)
                .setMaxResults(3)
                .getResultList();
    }

    //행정구역으로 식당 조회
    public List<Restaurant> findRestaurantByAddress(String address_name) {
        return em.createQuery("select r from Restaurant r where r.address_name like :address_name", Restaurant.class)
                .setParameter("address_name", "%" + address_name + "%")
                .getResultList();
    }

    //식당이름으로 조회
    public List<Restaurant> findByName(String name) {
        return em.createQuery("select r from Restaurant r where r.name like :name", Restaurant.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

    //통합검색
    public Slice<Restaurant> searchByNameAndAddress(String keyword, Pageable pageable) {
        List<Restaurant> restaurantList = em.createQuery("select r from Restaurant r where r.name like :keyword or r.address_name like :keyword", Restaurant.class)
                .setParameter("keyword", keyword)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new SliceImpl<>(restaurantList, pageable, restaurantList.size() >= pageable.getPageSize());
    }

    //키워드를 포함하는 주소나 가게이름 조회
    public Slice<Restaurant> searchByKeywordContaining(String keyword, Pageable pageable) {
        List<Restaurant> restaurantList = em.createQuery("select r from Restaurant r where r.name like CONCAT('%', :keyword, '%') or " +
                        "r.address_name like CONCAT('%', :keyword, '%') order by r.avgRating desc", Restaurant.class)
                .setParameter("keyword", keyword)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new SliceImpl<>(restaurantList, pageable, restaurantList.size() >= pageable.getPageSize());
    }



    public void saveAll(List<Restaurant> restaurantList) {
        for (Restaurant restaurant : restaurantList) {
            em.persist(restaurant);
        }
    }

    public void delete(Long restaurantId) {
        em.remove(restaurantId);
    }


}
