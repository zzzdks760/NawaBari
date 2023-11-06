package com.backend.NawaBari.repository;

import com.backend.NawaBari.domain.BookMark;
import com.backend.NawaBari.domain.Restaurant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class BookMarkRepository {

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public void save(BookMark bookMark) {
        em.persist(bookMark);
    }

    //DB 에서 북마크 조회
    public BookMark findBookMark(Long memberId, Long restaurantId) {

        return em.createQuery("select b from BookMark b where b.member.id = :memberId and b.restaurant.id = :restaurantId", BookMark.class)
                .setParameter("memberId", memberId)
                .setParameter("restaurantId", restaurantId)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

    }

    //북마크 삭제
    @Transactional
    public void delete(Long bookMarkId) {
        em.createQuery("delete from BookMark where id = :bookMarkId")
                .setParameter("bookMarkId", bookMarkId)
                .executeUpdate();
    }


    //식당아이디로 북마크 조회
    public List<Long> findBookMarkMember(Long restaurantId) {
        return em.createQuery("select b.member.id from BookMark b where b.restaurant.id = :restaurantId", Long.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
    }

    //회원이 북마크한 식당 리스트 조회
    public Slice<Restaurant> findBookMarkRestaurants(Long memberId, Pageable pageable) {
        List<Restaurant> restaurants = em.createQuery("select r from BookMark b left join b.restaurant r where b.member.id = :memberId", Restaurant.class)
                .setParameter("memberId", memberId)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new SliceImpl<>(restaurants, pageable, restaurants.size() >= pageable.getPageSize());
    }
}
