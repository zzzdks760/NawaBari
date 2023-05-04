package com.backend.NawaBari.domain;

import com.backend.NawaBari.domain.review.Review;
import jakarta.persistence.*;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    private String password;

    private String profile_nickname;

    private String profile_image;

    private String gender;

    private String age;

    private String refreshToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberZone> memberZones = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Heart> hearts = new ArrayList<>();

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

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    @Builder
    public Member(String kakao_id, String email, String password, String profile_nickname, String profile_image, String gender, String age, String refreshToken, Role role, SocialType socialType) {
        this.kakao_id = kakao_id;
        this.email = email;
        this.password = password;
        this.profile_nickname = profile_nickname;
        this.profile_image = profile_image;
        this.gender = gender;
        this.age = age;
        this.refreshToken = refreshToken;
        this.role = role;
        this.socialType = socialType;
    }
}


