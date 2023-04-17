package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Zone;
import com.backend.NawaBari.dto.MemberDTO;
import com.backend.NawaBari.dto.ZoneDTO;
import com.backend.NawaBari.exception.MaximumZoneLimitException;
import com.backend.NawaBari.exception.ZoneAlreadySetException;
import com.backend.NawaBari.repository.MemberRepository;
import com.backend.NawaBari.repository.MemberZoneRepository;
import com.backend.NawaBari.repository.ZoneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberZoneServiceTest {

    @Autowired
    private MemberZoneService memberZoneService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private MemberZoneRepository memberZoneRepository;

/*    @Test
    public void 구역초과체크() throws Exception {
        // Given
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setKakao_id("123");
        memberDTO.setProfile_nickname("Kim");
        memberDTO.setProfile_image("url");
        memberDTO.setGender("male");
        memberDTO.setAge("15");

        ZoneDTO zoneDTO1 = new ZoneDTO();
        zoneDTO1.setCig_cd(10L);
        zoneDTO1.setGu("GangNam");
        zoneDTO1.setDong("Sang");
        zoneDTO1.setLat(11.11);
        zoneDTO1.setLng(22.22);

        ZoneDTO zoneDTO2 = new ZoneDTO();
        zoneDTO2.setCig_cd(20L);
        zoneDTO2.setGu("GangDong");
        zoneDTO2.setDong("Young");
        zoneDTO2.setLat(33.33);
        zoneDTO2.setLng(44.44);

        ZoneDTO zoneDTO3 = new ZoneDTO();
        zoneDTO3.setCig_cd(22L);
        zoneDTO3.setGu("GangBook");
        zoneDTO3.setDong("Sung");
        zoneDTO3.setLat(55.55);
        zoneDTO3.setLng(66.66);



        Member member = Member.toMemberEntity(memberDTO);
        Zone zone1 = Zone.toZoneEntity(zoneDTO1);
        Zone zone2 = Zone.toZoneEntity(zoneDTO2);
        Zone zone3 = Zone.toZoneEntity(zoneDTO3);

        memberRepository.save(member);
        zoneRepository.save(zone1);
        zoneRepository.save(zone2);
        zoneRepository.save(zone3);


        Member findMember = memberRepository.findOne(member.getId());
        Zone findZone1 = zoneRepository.findOne(zone1.getId());
        Zone findZone2 = zoneRepository.findOne(zone2.getId());
        Zone findZone3 = zoneRepository.findOne(zone3.getId());

        //when

        MemberDTO memDTO = MemberDTO.toMemberDTO(findMember);
        ZoneDTO zonDTO1 = ZoneDTO.toZoneDTO(findZone1);
        ZoneDTO zonDTO2 = ZoneDTO.toZoneDTO(findZone2);
        ZoneDTO zonDTO3 = ZoneDTO.toZoneDTO(findZone3);


        //Then
        memberZoneService.setMemberZone(memDTO, zonDTO1);
        memberZoneService.setMemberZone(memDTO, zonDTO2);

        assertThrows(MaximumZoneLimitException.class, () -> {
            memberZoneService.setMemberZone(memDTO, zonDTO3);
        });
    }
    @Test
    public void 구역중복체크() throws Exception {
        // Given
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setKakao_id("123");
        memberDTO.setProfile_nickname("Kim");
        memberDTO.setProfile_image("url");
        memberDTO.setGender("male");
        memberDTO.setAge("15");

        ZoneDTO zoneDTO1 = new ZoneDTO();
        zoneDTO1.setCig_cd(10L);
        zoneDTO1.setGu("GangNam");
        zoneDTO1.setDong("Sang");
        zoneDTO1.setLat(11.11);
        zoneDTO1.setLng(22.22);

        ZoneDTO zoneDTO2 = new ZoneDTO();
        zoneDTO2.setCig_cd(20L);
        zoneDTO2.setGu("GangDong");
        zoneDTO2.setDong("Young");
        zoneDTO2.setLat(33.33);
        zoneDTO2.setLng(44.44);

        ZoneDTO zoneDTO3 = new ZoneDTO();
        zoneDTO3.setCig_cd(22L);
        zoneDTO3.setGu("GangBook");
        zoneDTO3.setDong("Sung");
        zoneDTO3.setLat(55.55);
        zoneDTO3.setLng(66.66);


        Member member = Member.toMemberEntity(memberDTO);
        Zone zone1 = Zone.toZoneEntity(zoneDTO1);
        Zone zone2 = Zone.toZoneEntity(zoneDTO2);
        Zone zone3 = Zone.toZoneEntity(zoneDTO3);

        memberRepository.save(member);
        zoneRepository.save(zone1);
        zoneRepository.save(zone2);
        zoneRepository.save(zone3);


        Member findMember = memberRepository.findOne(member.getId());
        Zone findZone1 = zoneRepository.findOne(zone1.getId());
        Zone findZone2 = zoneRepository.findOne(zone2.getId());
        Zone findZone3 = zoneRepository.findOne(zone3.getId());

        //when

        MemberDTO memDTO = MemberDTO.toMemberDTO(findMember);
        ZoneDTO zonDTO1 = ZoneDTO.toZoneDTO(findZone1);
        ZoneDTO zonDTO2 = ZoneDTO.toZoneDTO(findZone2);
        ZoneDTO zonDTO3 = ZoneDTO.toZoneDTO(findZone3);


        //Then
        memberZoneService.setMemberZone(memDTO, zonDTO1);

        assertThrows(ZoneAlreadySetException.class, () -> {
            memberZoneService.setMemberZone(memDTO, zonDTO1);
        });


    }*/
}
