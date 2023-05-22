package com.backend.NawaBari.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberZoneController {

    @GetMapping("/api/memberZone")
    public String memberZoneForm() {
        return "memberZoneForm";
    }
}
