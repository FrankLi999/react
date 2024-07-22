package com.bpwizard.configjdbc.resource.controller;

import com.bpwizard.configjdbc.resource.dto.ApplicationProfile;
import com.bpwizard.configjdbc.resource.dto.ConfigData;
import com.bpwizard.configjdbc.resource.service.ConfigDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.web.csrf.CsrfToken;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spring/admin/resource")
public class ConfigDataController {
   private final ConfigDataService configDataService;

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
   public List<ConfigData> uploadJson(@RequestBody List<ConfigData> configData) {
       return configDataService.recreateAllAndFindAll(configData);
   }

   @PostMapping(path = "sql", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<ConfigData> losqSql(@RequestBody String sql) throws SQLException {
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
