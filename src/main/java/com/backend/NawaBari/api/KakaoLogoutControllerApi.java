package com.backend.NawaBari.api;

import com.backend.NawaBari.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class KakaoLogoutControllerApi {

    private final MemberService memberService;

    @PostMapping("/api/v1/logout")
    public ResponseEntity<String> Logout(@RequestBody LogoutRequestDTO requestDTO) {
        Long memberId = requestDTO.getMemberId();
        memberService.Logout(memberId);

        return ResponseEntity.ok("Logout");
    }

    @Data
    static class LogoutRequestDTO {
        private Long memberId;
    }
}
