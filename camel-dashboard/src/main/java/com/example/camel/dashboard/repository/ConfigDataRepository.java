package com.example.camel.dashboard.repository;
import java.util.List;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.List;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import com.example.camel.dashboard.entity.ConfigDataEntity;

public interface ConfigDataRepository extends ReactiveCrudRepository<ConfigDataEntity, Long> {
    @Query("SELECT * FROM APP_PROPERTIES WHERE application = :application")
    Flux<List<ConfigDataEntity>> findConfigData(String application);
}
