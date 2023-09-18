package com.example.camel.dashboard.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.example.camel.dashboard.dto.ConfigData;
import com.example.camel.dashboard.entity.ConfigDataEntity;
import com.example.camel.dashboard.service.ConfigDataService;

@RestController
@RequestMapping("/api/config-data")
public class ConfigDataController {
    @Autowired
    private ConfigDataService configDataService;
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<ConfigData> findAll() {
        return configDataService.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> post(@RequestBody List<ConfigData> configData) {
        return configDataService.createAll(configData);
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> deleteApplications(@RequestBody List<ConfigData> configData) {
        return configDataService.deleteByIds(configData);
    }
}
