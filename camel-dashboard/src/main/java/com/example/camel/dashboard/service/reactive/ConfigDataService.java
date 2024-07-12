package com.example.camel.dashboard.service.reactive;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import com.example.camel.dashboard.dto.ApplicationProfile;
import com.example.camel.dashboard.dto.ConfigData;
import com.example.camel.dashboard.dto.ConfigurationProperty;
import com.example.camel.dashboard.entity.r2dbc.ConfigDataEntity;
import com.example.camel.dashboard.repository.r2dbc.ConfigDataRepository;

@Service
public class ConfigDataService {
    
    @Autowired ConfigDataRepository configDataRepository;
    @Autowired DatabaseClient databaseClient;
    // @Autowired DataSource dataSource;
    
    @Transactional
    public Mono<Void> createAll(final List<ConfigData> configData) {
        return this.saveAll(configData);
    }

    @Transactional
    public Mono<Void> recreateAll(final List<ConfigData> configData) {
        return this.deleteProfiles(configData).then(this.saveAll(configData));
    }

    @Transactional
    public Flux<ConfigData> recreateAllAndFindAll(final List<ConfigData> configData) {
        return this.deleteProfiles(configData).then(this.saveAll(configData)).thenMany(findAll());
    }

    public Flux<ConfigData> findAll() {
        return this.configDataRepository.findAll(orderByApplicationProfileAndLabel()).groupBy(this::getKey).map(g -> g.collectList().map(this::toConfigData)).flatMap(c -> c).sort();
    }

    @Transactional
    public Flux<ConfigData> deleteApplicationProfiles(final List<ApplicationProfile> applicationProfiles) {
        return Flux.fromIterable(applicationProfiles).flatMap(a -> this.configDataRepository.deleteApplicationProfile(a.getApplication(), a.getProfile())).then().thenMany(this.findAll());
    }

    // @Transactional
    // public Mono<Void> updateApplications(final List<ConfigData> configData) {
    //     List<com.example.camel.dashboard.entity.mvc.ConfigDataEntity> configDateEntries = configData.stream().flatMap(config -> config.getProps().stream().map(prop -> createConfigDataEntity(config, prop))).collect(Collectors.toUnmodifiableList());
    //     return Flux.fromIterable(configDateEntries).flatMap(e -> this.configDataRepository.updatePropertyValue(e.getApplication(), e.getProfile(), e.getLabel(), e.getPropKey(), e.getPropValue())).then();
    // }

    // @Transactional
    // public Mono<Void> deletePropertyValues(final ConfigData config) {
    //     List<com.example.camel.dashboard.entity.mvc.ConfigDataEntity> configDateEntries = config.getProps().stream().map(prop -> createConfigDataEntity(config, prop)).collect(Collectors.toUnmodifiableList());
    //     return Flux.fromIterable(configDateEntries).flatMap(e -> this.configDataRepository.deletePropertyValue(e.getApplication(), e.getProfile(), e.getLabel(), e.getPropKey())).then();
    // }

    // @Transactional
    // public Mono<Void> deleteByIds(final List<ConfigData> configData) {
    //     List<Long> ids = configData.stream().flatMap(config-> config.getProps().stream().map(prop -> prop.getId())).collect(Collectors.toUnmodifiableList());
    //     return this.configDataRepository.deleteAllById(ids);
    // }

    public Flux<ConfigData> loadSql(final String sql) throws SQLException {
        // return Mono.fromCallable(() -> {
        //     ScriptRunner scriptRunner = new ScriptRunner(dataSource.getConnection());
        //     scriptRunner.setSendFullScript(false);
        //     scriptRunner.setStopOnError(true);
        //     scriptRunner.runScript(new StringReader(sql));
        //     return Boolean.TRUE;
        // }).thenMany(findAll());
        throw new RuntimeException("No Impl");
    }
    private Mono<Void> deleteProfiles(final List<ConfigData> configData) {
        List<ApplicationProfile> applicationProfiles = configData.stream().map(config -> createApplicationProfile(config)).collect(Collectors.toUnmodifiableList());
        return Flux.fromIterable(applicationProfiles).flatMap(a -> this.configDataRepository.deleteApplicationProfile(a.getApplication(), a.getProfile())).then();
    }
    
    private Mono<Void> saveAll(final List<ConfigData> configData) {
        List<ConfigDataEntity> configDateEntries = configData.stream().flatMap(config -> config.getProps().stream().map(prop -> createConfigDataEntity(config, prop))).collect(Collectors.toUnmodifiableList());
        return this.configDataRepository.saveAll(configDateEntries).then();
    }

    private ConfigDataEntity createConfigDataEntity(ConfigData configData, ConfigurationProperty prop) {        
        return ConfigDataEntity.builder().application(configData.getApplication()).profile(configData.getProfile()).label(configData.getLabel()).propKey(prop.getPropKey()).propValue(prop.getPropValue()).build();
    }

    private ApplicationProfile createApplicationProfile(ConfigData configData) {
        return ApplicationProfile.builder().application(configData.getApplication()).profile(configData.getProfile()).build();
    }

    private String getKey(ConfigDataEntity entity) {
        return String.format("%s~~%s~~%s", entity.getApplication(), entity.getProfile(), entity.getLabel());
    }

    private ConfigurationProperty toConfigurationProperty(ConfigDataEntity entity) {
        return ConfigurationProperty.builder().propKey(entity.getPropKey()).propValue(entity.getPropValue()).build();
    }

    private ConfigData toConfigData(List<ConfigDataEntity> entities) {
        if (ObjectUtils.isEmpty(entities)) {
            return null;
        }
        ConfigDataEntity entity = entities.get(0);
        return ConfigData.builder().key(String.format("%s,%s", entity.getApplication(), entity.getProfile())).application(entity.getApplication()).profile(entity.getProfile()).label(entity.getLabel()).props(entities.stream().map(this::toConfigurationProperty).collect(Collectors.toList())).build();
    }

    private Sort orderByApplicationProfileAndLabel() {
        return Sort.by("application").ascending().and(Sort.by("profile").ascending()).and(Sort.by("label").ascending()).and(Sort.by("propKey").ascending());
    }
}
