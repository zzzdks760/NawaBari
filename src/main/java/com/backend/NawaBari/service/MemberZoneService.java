package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.MemberZone;
import com.backend.NawaBari.domain.Zone;
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
@Transactional
@RequiredArgsConstructor
public class MemberZoneService {

    private final MemberZoneRepository memberZoneRepository;
    private final ZoneRepository zoneRepository;

    /**
     * 영역 목록 추가
     */
    public void setMemberZone(Member member, ZoneDTO zoneDTO) {
        if (member.getMemberZones().size() >= 2) {
            throw new MaximumZoneLimitException("이미 두 개의 구역이 설정되어 있습니다");
        }

        Zone zone = zoneRepository.findOne(zoneDTO.getCig_cd());

        boolean zoneAlreadySet = member.getMemberZones().stream()
                .anyMatch(memberZone -> memberZone.getZone().getCig_cd().equals(zone.getCig_cd()));

        if (zoneAlreadySet) {
            throw new ZoneAlreadySetException("이미 설정 된 구역입니다");
        }

        MemberZone memberZone = MemberZone.builder()
                .member(member)
                .zone(zone)
                .build();

        memberZoneRepository.save(memberZone);
    }

}