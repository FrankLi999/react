package com.example.camel.dashboard.controller;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBufferUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.example.camel.dashboard.dto.ConfigData;
import com.example.camel.dashboard.dto.ApplicationProfile;
import com.example.camel.dashboard.entity.ConfigDataEntity;
import com.example.camel.dashboard.service.ConfigDataService;

@RestController
@RequestMapping("/api")
public class ConfigDataController {
    @Autowired
    private ConfigDataService configDataService;
    @Autowired ObjectMapper objectMapper;

    @GetMapping(path="/configurations", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<ConfigData> findAll() {
        return configDataService.findAll();
    }

    @PostMapping(path="/configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> post(@RequestBody List<ConfigData> configData) {
        return configDataService.createAll(configData);
    }

    @PostMapping(path = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<ConfigData> upload(@RequestPart("file") Mono<FilePart> configFilePartMono) {
        return  configFilePartMono
                .flatMap(fp -> metaDataFileContentToString(fp))  
                .map(str -> {
                    List<ConfigData> configData = null;
                    try {
                        configData = objectMapper.readValue(str, new TypeReference<List<ConfigData>>(){});
                    } catch (JsonProcessingException ex) { // TODO
                        ex.printStackTrace();
                        configData = Collections.emptyList(); 
                    }
                    return configData;
                })  
                .flatMapMany(configData -> configDataService.recreateAllAndFindAll(configData));
    }

    @PutMapping(path="/configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> put(@RequestBody List<ConfigData> configData) {
        return configDataService.recreateAll(configData);
    }

    @DeleteMapping(path="/configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<ConfigData> deleteApplicationProfiles(@RequestBody List<ApplicationProfile> applicationProfiles) {
        return configDataService.deleteApplicationProfiles(applicationProfiles);
    }


    private Mono<String> metaDataFileContentToString(FilePart metaDataFile) {
        return metaDataFile.content()
            .map(buffer -> buffer.toString(StandardCharsets.UTF_8))
            .collectList()
            .map(list -> String.join("", list));
    }
}
