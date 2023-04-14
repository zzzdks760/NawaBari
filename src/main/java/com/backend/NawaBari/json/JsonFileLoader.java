package com.backend.NawaBari.json;
import java.io.FileReader;
import java.io.IOException;

import com.backend.NawaBari.domain.Restaurant;
import com.backend.NawaBari.domain.Zone;
import com.backend.NawaBari.dto.ZoneDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JsonFileLoader {

    public List<Zone> loadJsonData() throws IOException {

        Reader reader = new FileReader("src/main/resources/seoul.json");

        Gson gson = new Gson();
        List<ZoneDTO> zoneDTOList = gson.fromJson(reader, new TypeToken<List<ZoneDTO>>() {}.getType());
        System.out.println("zoneDTOList: "+zoneDTOList);

        return ZoneDTO.toEntityList(zoneDTOList);

    }

    public List<Restaurant> loadRestaurantJsonData() throws IOException {

        Reader reader = new FileReader("src/main/resources/restaurant.json");

        Gson gson = new Gson();
        List<Restaurant> restaurantList = gson.fromJson(reader, new TypeToken<List<Restaurant>>() {}.getType());
        System.out.println("restaurantList: "+restaurantList);

        return restaurantList;

    }
}
