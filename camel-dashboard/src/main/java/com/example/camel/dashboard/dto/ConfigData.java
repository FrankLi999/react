package com.example.camel.dashboard.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
// import lombok.extern.jackson.Jacksonized;

@Data
@Builder
//@Jacksonized
public class ConfigData {
    private String application;
    private String profile;
    private String label;
    private List<ConfigurationProperty> props;
}
