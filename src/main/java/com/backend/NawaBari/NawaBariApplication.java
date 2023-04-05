package com.backend.NawaBari;

import com.backend.NawaBari.json.JsonFileLoader;
import com.backend.NawaBari.repository.ZoneRepository;
import com.backend.NawaBari.service.ZoneService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
@SpringBootApplication
public class NawaBariApplication {

	public static void main(String[] args) {
		SpringApplication.run(NawaBariApplication.class, args);

	}

}
