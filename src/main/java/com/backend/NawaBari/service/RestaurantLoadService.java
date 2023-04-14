package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.json.JsonFileLoader;
import com.backend.NawaBari.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantLoadService {

    private final JsonFileLoader jsonFileLoader;
    private final RestaurantRepository restaurantRepository;

    public void SaveData() throws IOException {

        List<Restaurant> zoneList = jsonFileLoader.loadRestaurantJsonData();
        restaurantRepository.saveAll(zoneList);
    }

}
