package com.example.camel.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.camel.dashboard.dto.ConfigData;
import com.example.camel.dashboard.service.ConfigDataService;

@RestController
@RequestMapping("/api/config-data")
public class ConfigDataController {
    @Autowired
    private ConfigDataService configDataService;
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ConfigData configData) {
        configDataService.create(configData);
    }
}
