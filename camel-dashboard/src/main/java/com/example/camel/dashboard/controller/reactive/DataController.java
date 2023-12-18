package com.example.camel.dashboard.controller.reactive;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import com.example.camel.dashboard.dto.ConfigData;
import com.example.camel.dashboard.dto.ApplicationProfile;
import com.example.camel.dashboard.service.reactive.ConfigDataService;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/config")
public class ConfigDataController implements WebMvcConfigurer {
    @Autowired
    private ConfigDataService configDataService;
    @Autowired ObjectMapper objectMapper;

    @Autowired
    public ConfigDataController(ConfigDataService configDataService, ObjectMapper objectMapper) {
        this.configDataService = configDataService;
        this.objectMapper = objectMapper;
    }

    @GetMapping(name = "", produces = MediaType.TEXT_HTML_VALUE)
    public RedirectView forwardHawtioRequestToIndexHtml(RedirectAttributes attributes) {
//        attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
//        attributes.addAttribute("attribute", "redirectWithRedirectView");
        return new RedirectView("/s2i/integrator/index.html");
    }

    @GetMapping(path="/api/configurations", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ConfigData> findAll() {
        return configDataService.findAll();
    }

    @PostMapping(path="/api/configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void post(@RequestBody List<ConfigData> configData) {
        configDataService.createAll(configData);
    }

    @PostMapping(path = "/api/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ConfigData> upload(@RequestParam("file") MultipartFile file) throws IOException, JsonProcessingException {
        List<ConfigData> configData = objectMapper.readValue(file.getInputStream(), new TypeReference<List<ConfigData>>(){});
        return configDataService.recreateAllAndFindAll(configData);
    }

    @PutMapping(path="/api/configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void put(@RequestBody List<ConfigData> configData) {
        configDataService.recreateAll(configData);
    }

    @DeleteMapping(path="/api/configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ConfigData> deleteApplicationProfiles(@RequestBody List<ApplicationProfile> applicationProfiles) {
        return configDataService.deleteApplicationProfiles(applicationProfiles);
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        // @formatter:off
        // S2i Integrator React static resources
        System.out.println(">>>>>>>>>>>>addResourceHandlers >>>>>>>>>>>>");
        registry
                .addResourceHandler("/s2i/integrator/static/**")
                .addResourceLocations("classpath:/s2i-integrator-static/static/");
        registry
                .addResourceHandler("/s2i/integrator/img/**")
                .addResourceLocations("classpath:/s2i-integrator-static/img/");
        registry
                .addResourceHandler("/s2i/integrator/**")
                .addResourceLocations("classpath:/s2i-integrator-static/");

        // @formatter:on
    }
}
