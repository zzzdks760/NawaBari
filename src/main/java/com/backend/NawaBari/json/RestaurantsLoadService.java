package com.backend.NawaBari.json;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.Zone;
import com.backend.NawaBari.dto.RestaurantDTO;
import com.backend.NawaBari.dto.ZoneDTO;
import com.backend.NawaBari.repository.RestaurantRepository;
import com.backend.NawaBari.repository.ZoneRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantsLoadService {
    private final JsonFileLoader jsonFileLoader;
    private final RestaurantRepository restaurantRepository;

/*    @Transactional
    public void SaveData() throws IOException {

        List<Restaurant> restaurants = jsonFileLoader.restaurantLoadJsonData();
        restaurantRepository.saveAll(restaurants);
    }*/
}
