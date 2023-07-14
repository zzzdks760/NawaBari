package com.backend.NawaBari.api;

import com.backend.NawaBari.domain.Member;
import com.backend.NawaBari.domain.Role;
import com.backend.NawaBari.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MyPageApiController {
    private final MemberService memberService;

    /**
     * 마이페이지 조회
     */
    @GetMapping("/api/v1/MyPage/{memberId}")
    public MyPageDTO MyPage(@PathVariable Long memberId) {
        Member profile = memberService.profile(memberId);

        return new MyPageDTO(profile.getProfile_nickname(), profile.getProfile_image(), profile.getRole());
    }


    @Data
    @AllArgsConstructor
    static class MyPageDTO {
        private String profile_nickname;
        private String profile_image;
        private Role role;
    }
}
