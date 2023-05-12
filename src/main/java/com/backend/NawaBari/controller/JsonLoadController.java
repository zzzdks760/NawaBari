package com.backend.NawaBari.controller;


import com.backend.NawaBari.json.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class JsonLoadController {
    private final ZoneService zoneService;

/*    @GetMapping("/zone/save")
    public void saveZone() throws IOException {
        zoneService.SaveData();
    }*/

}

