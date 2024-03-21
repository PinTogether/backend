package com.pintogether.backend.config;

import com.pintogether.backend.repository.CollectionRepository;
import com.pintogether.backend.repository.PinRepository;
import com.pintogether.backend.repository.PlaceRepository;
import com.pintogether.backend.repository.StarRepository;
import com.pintogether.backend.service.CollectionService;
import com.pintogether.backend.service.InterestingCollectionService;
import com.pintogether.backend.service.SearchService;
import com.pintogether.backend.service.SqlPlaceSearchImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public SearchService placeSearch() {
        return new SqlPlaceSearchImpl();
    }

}
