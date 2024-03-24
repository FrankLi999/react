package com.bpwizard.configjdbc.controller;

import com.bpwizard.configjdbc.dto.ApplicationProfile;
import com.bpwizard.configjdbc.dto.ConfigData;
import com.bpwizard.configjdbc.service.ConfigDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.web.csrf.CsrfToken;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/spring/admin/api")
public class ConfigDataController {
   @Autowired
   private ConfigDataService configDataService;
   @Autowired ObjectMapper objectMapper;

   @Autowired
   public ConfigDataController(ConfigDataService configDataService, ObjectMapper objectMapper) {
       this.configDataService = configDataService;
       this.objectMapper = objectMapper;
   }

   @GetMapping(path="configurations", produces = MediaType.APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.OK)
   public List<ConfigData> findAll() {
       return configDataService.findAll();
   }

   @PostMapping(path="configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.CREATED)
   public void post(@RequestBody List<ConfigData> configData) {
       configDataService.createAll(configData);
   }

   @PostMapping(path = "import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.OK)
   public List<ConfigData> upload(@RequestParam("file") MultipartFile file) throws IOException, JsonProcessingException {
       List<ConfigData> configData = objectMapper.readValue(file.getInputStream(), new TypeReference<List<ConfigData>>(){});
       return configDataService.recreateAllAndFindAll(configData);
   }

   @PostMapping(path = "sql", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ConfigData> losqSql(@RequestParam("file") MultipartFile file) throws IOException, SQLException {
        String sql = new String(file.getBytes());
        return configDataService.loadSql(sql);
    }
   
   @PutMapping(path="configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.CREATED)
   public void put(@RequestBody List<ConfigData> configData) {
       configDataService.recreateAll(configData);
   }

   @DeleteMapping(path="configurations", consumes = MediaType.APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.OK)
   public List<ConfigData> deleteApplicationProfiles(@RequestBody List<ApplicationProfile> applicationProfiles) {
       return configDataService.deleteApplicationProfiles(applicationProfiles);
   }

       /**
     *
     * @param token
     * @return response like,     *
     * {
     *   "token": "d-lp0ZL5WLrtOMzo7_xzBnEkpHh8aL6uW0EBjPcyaHDjSasETttc5fPBYI7ADaqJjtFHNhQViRpLXo-Da3gw6sFTDhHVeZlh",
     *   "parameterName": "_csrf",
     *   "headerName": "X-CSRF-TOKEN"
     * }
     *
     *
     * import axios from 'axios';
     *
     * function post(path, data) {
     *     return axios.get("/csrf")
     *             .then(tokenResp => {
     *                 let config = {
     *                     headers: {
     *                         'X-CSRF-TOKEN': tokenResp.data.token,
     *                     }
     *                   }
     *                 return axios.post(path, data, config);
     *             })
     *             .then(res => res.data)
     * }
     */
    @PostMapping("csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }
}
