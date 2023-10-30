package com.backend.NawaBari.json;
import java.io.FileReader;
import java.io.IOException;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.Zone;
import com.backend.NawaBari.dto.RestaurantDTO;
import com.backend.NawaBari.dto.RestaurantJsonDTO;
import com.backend.NawaBari.dto.ZoneDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JsonFileLoader {

    public List<Zone> zoneLoadJsonData() throws IOException {

        Reader reader = new FileReader("/home/ubuntu/dev/NawaBari/src/main/resources/seoul.json");

        Gson gson = new Gson();
        List<ZoneDTO> zoneDTOList = gson.fromJson(reader, new TypeToken<List<ZoneDTO>>() {}.getType());

        return ZoneDTO.toEntityList(zoneDTOList);

    }

    public List<Restaurant> restaurantLoadJsonData() throws IOException {
        Reader reader = new FileReader("/home/ubuntu/dev/NawaBari/src/main/resources/seoul_restaurants.json");

        Gson gson = new Gson();
        List<RestaurantJsonDTO> restaurantDTOList = gson.fromJson(reader, new TypeToken<List<RestaurantDTO>>() {}.getType());

        return restaurantDTOList.stream()
                .map(RestaurantJsonDTO::toEntity)
                .collect(Collectors.toList());
    }
}
