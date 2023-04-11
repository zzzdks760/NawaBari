package com.backend.NawaBari.repository;

import com.backend.NawaBari.domain.Zone;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ZoneRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(Zone zone) {
        em.persist(zone);
    }


    //아이디로 행정구역조회
    public Zone findOne(Long zone_id) {
        return em.find(Zone.class, zone_id);
    }

    //구이름으로 행정구역찾기
    public List<Zone> findByGu(String gu) {
        return em.createQuery("select z From Zone z where z.gu = :gu", Zone.class)
                .setParameter("gu", gu)
                .getResultList();
    }

    //동이름으로 행정구역 찾기
    public List<Zone> findByDong(String dong) {
        return em.createQuery("select z from Zone z where z.dong = :dong", Zone.class)
                .setParameter("dong", dong)
                .getResultList();
    }

    //서울특별시 행정구역 코드, 구, 동, 위도, 경도 저장
    @Transactional
    public void saveAll(List<Zone> zoneList) {
        for (Zone zone : zoneList) {
            em.persist(zone);
        }
    }

}
