package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.dto.MyPageDTO;
import com.backend.NawaBari.repository.BookMarkRepository;
import com.backend.NawaBari.repository.MemberRepository;
import com.backend.NawaBari.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BookMarkRepository bookMarkRepository;

    /**
     * 회원정보 출력
     */
    public MyPageDTO getProfile(Long id) {
        Member member = memberRepository.findOne(id);
        List<Long> zoneIds = memberRepository.findZoneId(id);
        List<String> dongNames = memberRepository.findMemberIdByDongName(id);

        return new MyPageDTO(member.getProfile_nickname(), member.getProfile_image(), member.getRole(), zoneIds, dongNames);
    }


    /**
     * 회원정보 수정
     */
    @Transactional
    public void UpdateMyPage(Long id, String profile_nickname, String profile_image) {
        Member member = memberRepository.findOne(id);

        member.setProfile_nickname(profile_nickname);
        member.setProfile_image(profile_image);
    }

    /**
     * 회원 단건 조회
     */
    public Member findOne(Long id) {
        Member member = memberRepository.findOne(id);
        return member;
    }

    /**
     * 로그아웃(리프레시 토큰제거)
     */
    @Transactional
    public void Logout(Long id) {
        Member member = memberRepository.findOne(id);
        member.setRefreshToken(null);
        memberRepository.save(member);
    }
}