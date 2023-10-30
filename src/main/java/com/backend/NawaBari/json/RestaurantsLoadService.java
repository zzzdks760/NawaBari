package com.backend.NawaBari.json;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantsLoadService {
    private final JsonFileLoader jsonFileLoader;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void SaveData() throws IOException {

        List<Restaurant> restaurants = jsonFileLoader.restaurantLoadJsonData();
        restaurantRepository.saveAll(restaurants);
    }
}
