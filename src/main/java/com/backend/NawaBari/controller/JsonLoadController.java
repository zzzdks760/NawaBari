package com.backend.NawaBari.controller;

import com.backend.NawaBari.service.RestaurantLoadService;
import com.backend.NawaBari.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class JsonLoadController {
    private final ZoneService zoneService;
    private final RestaurantLoadService restaurantLoadService;

/*    @GetMapping("/zone/save")
    public void saveZone() throws IOException {
        zoneService.SaveData();
    }

    @GetMapping("/restaurant/save")
    public void saveRestaurant() throws IOException {
        restaurantLoadService.SaveData();
    }*/
}
