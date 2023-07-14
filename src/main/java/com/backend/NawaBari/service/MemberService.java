package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원정보 출력
     */
    public Member profile(Long id) {
        Member member = memberRepository.findOne(id);
        return member;
    }


    /**
     * 회원정보 수정
     */
    public void UpdateMypage(Long id) {
        Member member = memberRepository.findOne(id);

        member.setProfile_nickname(member.getProfile_nickname());
        member.setProfile_image(member.getProfile_image());
    }

    /**
     * 회원 단건 조회
     */
    public Member findOne(Long id) {
        Member member = memberRepository.findOne(id);
        return member;
    }
}