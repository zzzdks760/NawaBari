package com.backend.NawaBari.controller;

import com.backend.NawaBari.dto.ZoneDTO;
import com.backend.NawaBari.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/zone")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @GetMapping("/save")
    public void saveZone() throws IOException {
        zoneService.SaveData();
    }
}
