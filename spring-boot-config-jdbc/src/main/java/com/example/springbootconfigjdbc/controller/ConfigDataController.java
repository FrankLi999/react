package com.example.springbootconfigjdbc.controller;

import com.example.springbootconfigjdbc.dto.ApplicationProfile;
import com.example.springbootconfigjdbc.dto.ConfigData;
import com.example.springbootconfigjdbc.service.ConfigDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class ConfigDataController {
   @Autowired
   private ConfigDataService configDataService;
   @Autowired ObjectMapper objectMapper;

   @Autowired
   public ConfigDataController(ConfigDataService configDataService, ObjectMapper objectMapper) {
       this.configDataService = configDataService;
       this.objectMapper = objectMapper;
   }

   @GetMapping(path="api/configurations", produces = MediaType.APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.OK)
   public List<ConfigData> findAll() {
       return configDataService.findAll();
   }

   @PostMapping(path="api/configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.CREATED)
   public void post(@RequestBody List<ConfigData> configData) {
       configDataService.createAll(configData);
   }

   @PostMapping(path = "api/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.OK)
   public List<ConfigData> upload(@RequestParam("file") MultipartFile file) throws IOException, JsonProcessingException {
       List<ConfigData> configData = objectMapper.readValue(file.getInputStream(), new TypeReference<List<ConfigData>>(){});
       return configDataService.recreateAllAndFindAll(configData);
   }

   @PutMapping(path="api/configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.CREATED)
   public void put(@RequestBody List<ConfigData> configData) {
       configDataService.recreateAll(configData);
   }

   @DeleteMapping(path="api/configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.OK)
   public List<ConfigData> deleteApplicationProfiles(@RequestBody List<ApplicationProfile> applicationProfiles) {
       return configDataService.deleteApplicationProfiles(applicationProfiles);
   }
}
