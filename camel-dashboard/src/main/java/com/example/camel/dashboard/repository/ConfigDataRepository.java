package com.example.camel.dashboard.repository;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.data.r2dbc.repository.Query;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.example.camel.dashboard.entity.ConfigDataEntity;

public interface ConfigDataRepository extends ReactiveCrudRepository<ConfigDataEntity, Long>, ReactiveSortingRepository<ConfigDataEntity, Long> {
    @Query("SELECT * FROM APP_PROPERTIES WHERE application = :application")
    Flux<List<ConfigDataEntity>> findConfigData(String application);

    // Flux<ConfigDataEntity> findAll(Sort sort);
    
    @Modifying
    @Query("DELETE FROM APP_PROPERTIES WHERE application = :application and profile = :profile and label = :label and PROP_KEY = :propKey")
    Mono<Void> deletePropertyValue(String application, String profile, String label, String propKey);

    @Modifying
    @Query("DELETE FROM APP_PROPERTIES WHERE application = :application and profile = :profile")
    Mono<Void> deleteApplicationProfile(String application, String profile);
}
