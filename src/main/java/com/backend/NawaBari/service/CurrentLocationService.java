package com.backend.NawaBari.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CurrentLocationService {
    private final String kakaoMapApi = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json";

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoApiKey;

    public String getCurrentLocation(float current_lat, float current_lng) {
        String url = kakaoMapApi + "?x=" + current_lng + "&y=" + current_lat;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "KakaoAK " + kakaoApiKey)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            Gson gson = new Gson();
            JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);

            JsonArray documents = responseJson.getAsJsonArray("documents");

            if (documents.size() > 0) {
                String siName = documents.get(0).getAsJsonObject().get("region_1depth_name").getAsString();
                String dongName = documents.get(0).getAsJsonObject().get("region_3depth_name").getAsString();

                if (!siName.equals("서울특별시")) {
                    return null;
                }
                return dongName;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


    public LocationInfo getGuAndDong(float current_lat, float current_lng) {
        String url = kakaoMapApi + "?x=" + current_lng + "&y=" + current_lat;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "KakaoAK " + kakaoApiKey)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            Gson gson = new Gson();
            JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);

            JsonArray documents = responseJson.getAsJsonArray("documents");

            if (documents.size() > 0) {
                String siName = documents.get(0).getAsJsonObject().get("region_1depth_name").getAsString();
                String dongName = documents.get(0).getAsJsonObject().get("region_3depth_name").getAsString();
                String guName = documents.get(0).getAsJsonObject().get("region_2depth_name").getAsString();

                if (!siName.equals("서울특별시")) {
                    return null;
                }

                return new LocationInfo(dongName, guName);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Data
    public class LocationInfo {
        private String dongName;
        private String GuName;

        public LocationInfo(String dongName, String guName) {
            this.dongName = dongName;
            this.GuName = guName;
        }
    }
}
