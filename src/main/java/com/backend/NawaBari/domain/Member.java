package com.backend.NawaBari.domain;

import com.backend.NawaBari.domain.review.Review;
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

    private String email;

    private String profile_nickname;

    private String profile_image;

    private String gender;

    private String age;

    private String refreshToken;

    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberZone> memberZones = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<BookMark> bookMarks = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO

    // 유저 권한 설정 메소드
    public void authorizeMember() {
        this.role = Role.MEMBER;
    }

    //== 연관관계 메서드 ==//
    public void addMemberZone(MemberZone memberZone) {
        memberZones.add(memberZone);
        memberZone.setMember(this);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Builder
    public Member(Long id, String kakao_id, String email, String profile_nickname, String profile_image, String gender, String age, String refreshToken, String password, List<MemberZone> memberZones, List<Review> reviews, List<Heart> hearts, Role role, SocialType socialType) {
        this.id = id;
        this.kakao_id = kakao_id;
        this.email = email;
        this.profile_nickname = profile_nickname;
        this.profile_image = profile_image;
        this.gender = gender;
        this.age = age;
        this.refreshToken = refreshToken;
        this.password = password;
        this.memberZones = memberZones;
        this.reviews = reviews;
        this.hearts = hearts;
        this.role = role;
        this.socialType = socialType;
    }
}


