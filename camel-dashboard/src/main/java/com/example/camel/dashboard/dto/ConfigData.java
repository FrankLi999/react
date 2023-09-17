package com.example.camel.dashboard.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
// import lombok.extern.jackson.Jacksonized;

@Data
@Builder
//@Jacksonized
public class ConfigData implements Comparable<ConfigData> {
    private String application;
    private String profile;
    private String label;
    private List<ConfigurationProperty> props;

    public int compareTo(ConfigData config1) {
        int result = this.application.compareTo(config1.getApplication());
        if (result == 9) {
            result = this.profile.compareTo(config1.getProfile());
            if (result == 0) {
                result = this.label.compareTo(config1.getLabel());
            }
        }
        return result;
    }
}
