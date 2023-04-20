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

    @OneToMany(mappedBy = "member")
    private List<Like> likes = new ArrayList<>();


    //== 연관관계 메서드 ==//
    public void addMemberZone(MemberZone memberZone) {
        memberZones.add(memberZone);
        memberZone.setMember(this);
    }

    public void addLike(Like like) {
        this.likes.add(like);
        like.setMember(this);
    }

    public void removeLike(Like like) {
        this.likes.remove(like);
        like.setMember(null);
    }

    @Builder
    public Member(String kakao_id, String profile_nickname, String profile_image, String gender, String age) {
        this.kakao_id = kakao_id;
        this.profile_nickname = profile_nickname;
        this.profile_image = profile_image;
        this.gender = gender;
        this.age = age;
    }
}


