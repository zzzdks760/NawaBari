package com.backend.NawaBari.domain;

import com.backend.NawaBari.exception.MaximumZoneLimitException;
import com.backend.NawaBari.exception.ZoneAlreadySetException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberZone extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberzone_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @Builder
    public MemberZone(Member member, Zone zone) {
        this.member = member;
        this.zone = zone;
    }


    //== 연관관계 메서드 ==//
    public void setZone(Zone zone) {
        this.zone = zone;
        zone.getZones().add(this);
    }



    /**
     * 구역 설정
     */
    public static List<MemberZone> create(Member member, Zone zone) {
        checkMaximumZoneLimit(member);
        checkZoneAlreadySet(member, zone);

        MemberZone memberZone = new MemberZone();
        memberZone.setMember(member);
        memberZone.setZone(zone);
        member.getMemberZones().add(memberZone);

        List<MemberZone> memberZones = new ArrayList<>(member.getMemberZones());
        System.out.println("memberZones = " + memberZones);

        return memberZones;
    }

    /**
     * 초과체크
     */
    private static void checkMaximumZoneLimit(Member member) throws MaximumZoneLimitException{
        if (member.getMemberZones().size() >= 2) {
            throw new MaximumZoneLimitException("설정할 수 있는 구역을 초과 하였습니다.");
        }
    }

    /**
     * 중복체크
     */
    private static void checkZoneAlreadySet(Member member, Zone zone) throws ZoneAlreadySetException{
        boolean zoneAlreadySet = member.getMemberZones().stream()
                .anyMatch(memberZone -> memberZone.getZone().equals(zone));

        if (zoneAlreadySet) {
            throw new ZoneAlreadySetException("중복된 구역이 존재합니다.");
        }
    }

}

