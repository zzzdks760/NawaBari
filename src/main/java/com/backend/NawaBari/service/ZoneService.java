package com.backend.NawaBari.service;

import com.backend.NawaBari.domain.Zone;
import com.backend.NawaBari.dto.ZoneDTO;
import com.backend.NawaBari.json.JsonFileLoader;
import com.backend.NawaBari.repository.ZoneRepository;
import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ZoneService {

    private final JsonFileLoader jsonFileLoader;
    private final ZoneRepository zoneRepository;

/*    public void SaveData() throws IOException {

        List<Zone> zoneList = jsonFileLoader.loadJsonData();
        zoneRepository.saveAll(zoneList);
    }*/

}
