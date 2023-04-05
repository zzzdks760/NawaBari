package com.backend.NawaBari.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.backend.NawaBari.dto.ZoneDTO;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ZoneConverter {

    public static List<ZoneDTO> convertJsonToZoneDTOList(String jsonFilePath) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ZoneDTO.class, new ZoneDTODeserializer());
        Gson gson = gsonBuilder.create();

        Type zoneDTOListType = new TypeToken<List<ZoneDTO>>() {}.getType();

        try (FileReader reader = new FileReader(jsonFilePath)) {
            List<ZoneDTO> zoneDTOList = gson.fromJson(reader, zoneDTOListType);
            return zoneDTOList;
        }
    }

    public static void main(String[] args) {
        try {
            List<ZoneDTO> zoneDTOList = convertJsonToZoneDTOList("seoul.json");
            System.out.println(zoneDTOList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}