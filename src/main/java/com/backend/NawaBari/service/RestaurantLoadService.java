package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Base;
import com.backend.NawaBari.domain.Restaurant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantLoadService {



    private final String KAKAO_API_KEY = "27eb3015ee913717948781cb023560f3";
    private final int PAGE_SIZE = 15;

    public List<Restaurant> searchRestaurants(String query, int page) throws IOException {
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json";
        url += "?query=" + URLEncoder.encode(query, "UTF-8");
        url += "&page=" + page;
        url += "&size=" + PAGE_SIZE;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "KakaoAK " + KAKAO_API_KEY);
        con.setRequestProperty("Content-Type", "application/json");

        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            ObjectMapper objectMapper = new ObjectMapper();
            List<Restaurant> restaurantList = objectMapper.readValue(response.toString(), new TypeReference<List<Restaurant>>() {});
            return restaurantList;
        } else {
            throw new RuntimeException("Failed to retrieve restaurants from Kakao API: " + con.getResponseMessage());
        }
    }

}
