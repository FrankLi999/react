package com.example.camel.dashboard.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.camel.dashboard.dto.ConfigData;
import com.example.camel.dashboard.dto.ConfigurationProperty;
import com.example.camel.dashboard.entity.ConfigDataEntity;
import com.example.camel.dashboard.repository.ConfigDataRepository;

@Service
public class ConfigDataService {
    
    @Autowired ConfigDataRepository configDataRepository;
    public void create(final ConfigData configData) {
        System.out.println(">>>>>>>>>>> config data:" + configData);
        List<ConfigDataEntity> configDateEntries = configData.getProps().stream().map(prop -> createConfigDataEntity(configData, prop)).collect(Collectors.toUnmodifiableList());
        this.configDataRepository.saveAll(configDateEntries);
    }

    private ConfigDataEntity createConfigDataEntity(ConfigData configData, ConfigurationProperty prop) {        
        return ConfigDataEntity.builder().application(configData.getApplication()).profile(configData.getProfile()).label(configData.getLabel()).propKey(prop.getPropKey()).propValue(prop.getPropValue()).build();
    }
}
