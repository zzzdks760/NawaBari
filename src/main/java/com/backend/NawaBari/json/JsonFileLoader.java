package com.backend.NawaBari.json;
import java.io.FileReader;
import java.io.IOException;

import com.backend.NawaBari.domain.Zone;
import com.backend.NawaBari.dto.ZoneDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class JsonFileLoader {

    public List<Zone> loadJsonData() throws IOException {

        Reader reader = new FileReader("src/main/resources/seoul.json");

        Gson gson = new Gson();
        List<ZoneDTO> zoneDTOList = gson.fromJson(reader, new TypeToken<List<ZoneDTO>>() {}.getType());

        return ZoneDTO.toEntityList(zoneDTOList);

    }
}
