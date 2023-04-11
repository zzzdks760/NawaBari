package com.backend.NawaBari.domain;

import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.dto.MemberDTO;
import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Base{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String kakao_id;

    private String profile_nickname;

    private String profile_image;

    private String gender;

    private String age;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberZone> memberZones = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Review> reviews = new ArrayList<>();


    //생성자 주입

    @Builder
    public Member(String kakao_id, String profile_nickname) {
        this.kakao_id = kakao_id;
        this.profile_nickname = profile_nickname;
    }

    public static Member toMemberEntity(MemberDTO memberDTO) {
        Member member = new Member();
        member.setKakao_id(memberDTO.getKakao_id());
        member.setProfile_nickname(memberDTO.getProfile_nickname());
        member.setProfile_image(memberDTO.getProfile_image());
        member.setGender(memberDTO.getGender());
        member.setAge(memberDTO.getAge());
        return member;
    }
}
