package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.MemberZone;
import com.backend.NawaBari.domain.Zone;
import com.backend.NawaBari.repository.MemberRepository;
import com.backend.NawaBari.repository.MemberZoneRepository;
import com.backend.NawaBari.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberZoneService {

    private final MemberZoneRepository memberZoneRepository;
    private final MemberRepository memberRepository;
    private final ZoneRepository zoneRepository;

    @Transactional
    public List<Long> setMemberZone(Long memberId, String guName, String dongName) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Zone zone = zoneRepository.findByGuAndDong(guName, dongName);


        //회원 구역설정
        List<MemberZone> memberZones = MemberZone.create(member, zone);

        //회원 구역저장
        memberZoneRepository.save(memberZones);

        //회원 구역 아이디 반환
        return memberZones.stream()
                .map(MemberZone::getId)
                .collect(Collectors.toList());

    }

}