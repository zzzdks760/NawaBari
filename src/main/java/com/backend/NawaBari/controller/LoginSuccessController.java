package com.backend.NawaBari.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginSuccessController {

    @GetMapping("/api/login/success")
    public String memberZoneForm() {
        return "loginSuccess";
    }
}
