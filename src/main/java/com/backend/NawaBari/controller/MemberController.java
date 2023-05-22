package com.backend.NawaBari.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
//    private final MemberService memberService;

/*
    @PostMapping("/sign-up")
    public String signUp(@RequestBody MemberSignUpDTO memberSignUpDTO) throws Exception {
        memberService.signUp(memberSignUpDTO);
        return "회원가입 성공";
    }
*/

    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }
}
