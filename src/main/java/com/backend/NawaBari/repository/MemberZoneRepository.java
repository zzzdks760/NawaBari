package com.backend.NawaBari.repository;

import com.backend.NawaBari.domain.MemberZone;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class MemberZoneRepository {

    @PersistenceContext
    private final EntityManager em;

    //제한된 구역저장
    @Transactional
    public void save(MemberZone memberZone) {
        em.persist(memberZone);
    }

    //아이디로 제한된 구역조회
    public MemberZone findOne(Long member_zone_id) {
        return em.find(MemberZone.class, member_zone_id);
    }

    //회원아이디로 선택한 제한된 구역 모두조회
    public List<MemberZone> findByMemberId(Long member_id) {
        return em.createQuery("select mz From MemberZone mz where mz.member_id = :member_id", MemberZone.class)
                .setParameter("member_id", member_id)
                .getResultList();
    }



}
