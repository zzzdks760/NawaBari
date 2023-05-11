/*
package com.backend.NawaBari.oauth;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Role;
import com.backend.NawaBari.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(MemberSignUpDTO memberSignUpDTO) throws Exception {

        if (memberRepository.findByEmail(memberSignUpDTO.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if (memberRepository.findByNickname(memberSignUpDTO.getProfile_nickname()).isPresent()) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        Member member = Member.builder()
                .email(memberSignUpDTO.getEmail())
                .password(memberSignUpDTO.getPassword())
                .profile_nickname(memberSignUpDTO.getProfile_nickname())
                .age(memberSignUpDTO.getAge())
                .gender(memberSignUpDTO.getGender())
                .role(Role.MEMBER)
                .build();

        member.passwordEncode(passwordEncoder);
        memberRepository.save(member);
    }
}
*/
