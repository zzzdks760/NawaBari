package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.MemberZone;
import com.backend.NawaBari.domain.Zone;
import com.backend.NawaBari.dto.MemberDTO;
import com.backend.NawaBari.dto.ZoneDTO;
import com.backend.NawaBari.exception.MaximumZoneLimitException;
import com.backend.NawaBari.exception.ZoneAlreadySetException;
import com.backend.NawaBari.repository.MemberRepository;
import com.backend.NawaBari.repository.MemberZoneRepository;
import com.backend.NawaBari.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberZoneService {

    private final MemberZoneRepository memberZoneRepository;
    private final MemberRepository memberRepository;
    private final ZoneRepository zoneRepository;

    @Transactional
    public Long setMemberZone(Long memberId, Long zoneId) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Zone zone = zoneRepository.findOne(zoneId);

        //회원 구역설정
        MemberZone memberZone = MemberZone.create(member, zone);

        //회원 구역저장
        memberZoneRepository.save(memberZone);

        //회원 구역 아이디 반환
        return memberZone.getId();
    }


    /**
     * 구역설정
     */
    public void setMemberZone(MemberDTO memberDTO, ZoneDTO zoneDTO) {
        Member member = memberRepository.findOne(memberDTO.getId());
        Zone zone = zoneRepository.findOne(zoneDTO.getId());

        checkZoneLimit(member);
        checkZoneAlreadySet(member, zone);

        setNewZone(member, zone);
    }


    /**
     * 설정 구역 초과체크
     */
    private void checkZoneLimit(Member member) {
        if (member.getMemberZones().size() >= 2) {
            throw new MaximumZoneLimitException("이미 두 개의 구역이 설정되어 있습니다");
        }
    }

    /**
     * 설정 구역 중복체크
     */
    private void checkZoneAlreadySet(Member member, Zone zone) {
        boolean zoneAlreadySet = member.getMemberZones().stream()
                .anyMatch(memberZone -> memberZone.getZone().getId().equals(zone.getId()));

        if (zoneAlreadySet) {
            throw new ZoneAlreadySetException("이미 설정 된 구역입니다");
        }
    }

    /**
     * 새 구역 추가
     */
    private void setNewZone(Member member, Zone zone) {
        MemberZone newMemberZone = MemberZone.builder()
                .member(member)
                .zone(zone)
                .build();

        member.getMemberZones().add(newMemberZone);
        memberZoneRepository.save(newMemberZone);
    }

}