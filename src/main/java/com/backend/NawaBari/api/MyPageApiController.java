package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Role;
import com.backend.NawaBari.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyPageApiController {
    private final MemberService memberService;

    @PostMapping("/api/v1/MyPage/{memberId}")
    public MyPageDTO MyPage(@PathVariable Long memberId) {
        Member profile = memberService.profile(memberId);

        MyPageDTO myPageDTO = new MyPageDTO();
        myPageDTO.setId(profile.getId());
        myPageDTO.setProfile_nickname(profile.getProfile_nickname());
        myPageDTO.setProfile_image(profile.getProfile_image());
        myPageDTO.setRole(profile.getRole());

        return myPageDTO;
    }


    @Data
    static class MyPageDTO {
        private Long id;
        private String profile_nickname;
        private String profile_image;
        private Role role;
    }
}
