package com.example.camel.dashboard.service.mvc;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.TreeMap;
//import java.util.stream.Collectors;
//
//import com.example.camel.dashboard.entity.ConfigDataEntityKey;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.example.camel.dashboard.dto.ApplicationProfile;
//import com.example.camel.dashboard.dto.ConfigData;
//import com.example.camel.dashboard.dto.ConfigurationProperty;
//import com.example.camel.dashboard.entity.ConfigDataEntity;
//import com.example.camel.dashboard.repository.com.example.camel.dashboard.repository.mvc.ConfigDataRepository;
//
//@Service
public class ConfigDataService {
//    private com.example.camel.dashboard.repository.mvc.ConfigDataRepository configDataRepository;
//    @Autowired
//    public ConfigDataService(com.example.camel.dashboard.repository.mvc.ConfigDataRepository configDataRepository) {
//        this.configDataRepository = configDataRepository;
//    }
//
//    @Transactional
//    public void createAll(final List<ConfigData> configData) {
//        this.saveAll(configData);
//    }
//
//    @Transactional
//    public void recreateAll(final List<ConfigData> configData) {
//        this.deleteProfiles(configData);
//        this.saveAll(configData);
//    }
//
//    @Transactional
//    public List<ConfigData> recreateAllAndFindAll(final List<ConfigData> configData) {
//        this.deleteProfiles(configData);
//        this.saveAll(configData);
//        return findAll();
//    }
//
//    public List<ConfigData> findAll() {
//        List<ConfigDataEntity> configDataEntities = this.configDataRepository.findAll();
//        List<ConfigData> configDataList = new ArrayList<>();
//        configDataEntities.stream().collect(Collectors.groupingBy(
//                c -> ConfigDataEntityKey.builder().application(c.getApplication()).profile(c.getProfile()).label(c.getLabel()).build(),
//                TreeMap::new,
//                Collectors.mapping(e -> ConfigurationProperty.builder().propKey(e.getPropKey()).propValue(e.getPropValue()).build(), Collectors.toList())))
//                .forEach((k, v) -> configDataList.add(ConfigData.builder().key(String.format("%s,%s", k.getApplication(), k.getProfile())).application(k.getApplication()).profile(k.getProfile()).label(k.getLabel()).props(v).build()));
//        return configDataList;
//
//    }
//
//    @Transactional
//    public List<ConfigData> deleteApplicationProfiles(final List<ApplicationProfile> applicationProfiles) {
//        applicationProfiles.forEach(a -> configDataRepository.deleteApplicationProfile(a.getApplication(), a.getProfile()));
//        return this.findAll();
//    }
//
//    private void deleteProfiles(final List<ConfigData> configData) {
//        List<ApplicationProfile> applicationProfiles = configData.stream().map(config -> createApplicationProfile(config)).collect(Collectors.toUnmodifiableList());
//        applicationProfiles.forEach(a -> configDataRepository.deleteApplicationProfile(a.getApplication(), a.getProfile()));
//    }
//
//    private void saveAll(final List<ConfigData> configData) {
//        List<ConfigDataEntity> configDateEntries = configData.stream().flatMap(config -> config.getProps().stream().map(prop -> createConfigDataEntity(config, prop))).collect(Collectors.toUnmodifiableList());
//        this.configDataRepository.saveAll(configDateEntries);
//    }
//
//    private ConfigDataEntity createConfigDataEntity(ConfigData configData, ConfigurationProperty prop) {
//        return ConfigDataEntity.builder().application(configData.getApplication()).profile(configData.getProfile()).label(configData.getLabel()).propKey(prop.getPropKey()).propValue(prop.getPropValue()).build();
//    }
//
//    private ApplicationProfile createApplicationProfile(ConfigData configData) {
//        return ApplicationProfile.builder().application(configData.getApplication()).profile(configData.getProfile()).build();
//    }
}
