package com.backend.NawaBari.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.backend.NawaBari.dto.ZoneDTO;

import java.lang.reflect.Type;

public class ZoneDTODeserializer implements JsonDeserializer<ZoneDTO> {

    @Override
    public ZoneDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Long cig_cd = jsonObject.get("cig_cd").getAsLong();
        String gu = jsonObject.get("gu").getAsString();
        String dong = jsonObject.get("dong").getAsString();
        Double lat = jsonObject.get("lat").getAsDouble();
        Double lng = jsonObject.get("lng").getAsDouble();

        return ZoneDTO.builder()
                .cig_cd(cig_cd)
                .gu(gu)
                .dong(dong)
                .lat(lat)
                .lng(lng)
                .build();
    }

}