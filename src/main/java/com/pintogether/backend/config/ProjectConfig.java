package com.pintogether.backend.config;

import com.pintogether.backend.service.SearchService;
import com.pintogether.backend.service.SqlPlaceSearchImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public SearchService placeSearch() {
        return new SqlPlaceSearchImpl();
    }

}
