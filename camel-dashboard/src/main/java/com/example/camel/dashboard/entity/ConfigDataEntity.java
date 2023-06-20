package com.example.camel.dashboard.entity;
import lombok.Builder;
import lombok.Data;
// import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
@Data
@Builder
@Table("MY_CONFIG_DATA")
public class ConfigDataEntity {
    // @Id
    // private Long id;
    private String application;
    private String profile;
    private String label;
    private String propKey;
    private String propValue;
}

