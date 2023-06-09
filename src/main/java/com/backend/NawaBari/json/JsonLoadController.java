package com.backend.NawaBari.json;


import com.backend.NawaBari.json.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class JsonLoadController {
    private final ZoneService zoneService;
    private final RestaurantsLoadService restaurantsLoadService;

    @GetMapping("/zone/save")
    public void saveZone() throws IOException {
        zoneService.SaveData();
    }

    @GetMapping("/restaurant/save")
    public void saveRestaurant() throws IOException {
        restaurantsLoadService.SaveData();
    }

}

