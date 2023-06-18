package com.example.camel.dashboard.entity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class ConfigDataEntity {
    @Id
    private Long id;
    private String application;
    private String profile;
    private String label;
    private String propKey;
    private String propValue;
}

