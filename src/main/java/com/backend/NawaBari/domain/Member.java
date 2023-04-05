package com.backend.NawaBari.domain;

import com.backend.NawaBari.domain.review.Review;
import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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
    public Member(String kakao_id, String profile_nickname, String profile_image, String gender, String age, List<MemberZone> memberZones, List<Review> reviews) {
        this.kakao_id = kakao_id;
        this.profile_nickname = profile_nickname;
        this.profile_image = profile_image;
        this.gender = gender;
        this.age = age;
        this.memberZones = memberZones;
        this.reviews = reviews;
    }

}
