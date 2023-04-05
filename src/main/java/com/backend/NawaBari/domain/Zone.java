package com.backend.NawaBari.domain;

import com.backend.NawaBari.domain.review.Review;
import com.backend.NawaBari.dto.ZoneDTO;
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
public class Zone extends Base{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zone_id")
    private Long id;

    private Long cig_cd; //행정구역 코드

    private String gu; //행정구역 속한 구

    private String dong; //행정구역 속한 동

    private Double lat; //행정구역 중심 위도

    private Double lng; //행정구역 중심 경도


    @OneToMany(mappedBy = "zone")
    private List<MemberZone> Zones = new ArrayList<>();

    @OneToMany(mappedBy = "zone")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "zone")
    private List<Restaurant> restaurants = new ArrayList<>();

    @Builder
    public Zone(Long cig_cd, String gu, String dong, Double lat, Double lng) {
        this.cig_cd = cig_cd;
        this.gu = gu;
        this.dong = dong;
        this.lat = lat;
        this.lng = lng;
    }
}
