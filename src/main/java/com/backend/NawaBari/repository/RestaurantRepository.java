package com.backend.NawaBari.repository;

import com.backend.NawaBari.domain.Restaurant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Restaurant restaurant) {
        em.persist(restaurant);
    }

    //행정구검색시 평점순으로 식당을 조회
    public List<Restaurant> findByZoneOrderByRating(String cig_name) {
        return em.createQuery("select r from Restaurant r where r.cig_name = :cig_name order by r.rate desc", Restaurant.class)
                .setParameter("cig_name", cig_name)
                .getResultList();
    }

    //식당이름으로 조회
    public List<Restaurant> findByName(String name) {
        return em.createQuery("select r from Restaurant r where r.name like :name", Restaurant.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

}
