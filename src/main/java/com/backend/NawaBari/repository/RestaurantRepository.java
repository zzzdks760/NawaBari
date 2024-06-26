package com.backend.NawaBari.repository;

import com.backend.NawaBari.domain.BookMark;
import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Menu;
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
        return em.createQuery("select r from Review r join fetch r.restaurant left join fetch r.photos where r.restaurant.id = :restaurant_id order by r.likeCount", Review.class)
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
        List<Restaurant> restaurantList = em.createQuery("select r from Restaurant r where r.name like :keyword or r.address_name like :keyword order by r.avgRating desc", Restaurant.class)
                .setParameter("keyword", keyword)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new SliceImpl<>(restaurantList, pageable, restaurantList.size() >= pageable.getPageSize());
    }

    //주소 검색
/*    public Slice<Restaurant> searchByAddress(String address, Pageable pageable) {
    List<Restaurant> restaurantList = em.createQuery("select r from Restaurant r where r.address_name like :address order by r.avgRating desc", Restaurant.class)
            .setParameter("address", address)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();

        return new SliceImpl<>(restaurantList, pageable, restaurantList.size() >= pageable.getPageSize());
    }
    //주소 검색 키워드가 포함된 식당 조회
    public Slice<Restaurant> searchByAddressContaining(String address, Pageable pageable) {
        List<Restaurant> restaurantList = em.createQuery("select r from Restaurant r where r.address_name like CONCAT('%', :address, '%') order by r.avgRating desc", Restaurant.class)
                .setParameter("address", address)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new SliceImpl<>(restaurantList, pageable, restaurantList.size() >= pageable.getPageSize());
    }

    //상호명 검색
    public Slice<Restaurant> searchByName(String name, Pageable pageable) {
        List<Restaurant> restaurantList = em.createQuery("select r from Restaurant r where r.name like :name order by r.avgRating desc", Restaurant.class)
                .setParameter("name", name)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new SliceImpl<>(restaurantList, pageable, restaurantList.size() >= pageable.getPageSize());
    }

    //상호명 검색 키워드가 포함된 식당 조회
    public Slice<Restaurant> searchByNameContaining(String name, Pageable pageable) {
        List<Restaurant> restaurantList = em.createQuery("select r from Restaurant r where r.name like CONCAT('%', :name, '%') order by r.avgRating desc", Restaurant.class)
                .setParameter("name", name)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new SliceImpl<>(restaurantList, pageable, restaurantList.size() >= pageable.getPageSize());
    }*/


    //동이름으로 식당찾기
    public Slice<Restaurant> searchByDongName(String dongName, Pageable pageable) {
        List<Restaurant> restaurantList = em.createQuery("select r from Restaurant r where r.address_name like :dongName order by r.avgRating desc", Restaurant.class)
                .setParameter("dongName", "%" + dongName + "%")
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


    @Transactional
    public void saveAll(List<Restaurant> restaurantList) {
        for (Restaurant restaurant : restaurantList) {
            em.persist(restaurant);
        }
    }

    @Transactional
    public void delete(Long restaurantId) {
        em.remove(restaurantId);
    }


    //리뷰아이디로 식당조회
    public Restaurant findRestaurantByReviewId(Long reviewId) {
        return em.createQuery("select r from Restaurant r left join fetch r.reviews rev where rev.id = :reviewId", Restaurant.class)
                .setParameter("reviewId", reviewId)
                .getSingleResult();
    }

    //식당아이디로 식당 메뉴조회
    public List<Menu> findMenu(Long restaurantId) {
        return em.createQuery("select m from Menu m where m.restaurant.id = :restaurantId", Menu.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
    }
}
