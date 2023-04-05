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

    //구역저장
    @Transactional
    public void save(Zone zone) {
        em.persist(zone);
    }

    //아이디로 행정구역조회
    public Zone findOne(Long zone_id) {
        return em.find(Zone.class, zone_id);
    }

    //구이름으로 행정구역찾기
    public List<Zone> findByName(String cig_name) {
        return em.createQuery("select z From Zone z where z.cig_name = :cig_name", Zone.class)
                .setParameter("cig_name", cig_name)
                .getResultList();
    }

    @Transactional
    public void saveAll(List<Zone> zoneList) {
        for (Zone zone : zoneList) {
            em.persist(zone);
        }
    }

}
