package com.example.camel.dashboard.entity.r2dbc;
import lombok.Builder;
import lombok.Data;
// import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
@Data
@Builder
public class ConfigDataEntityKey {
    // @Id
    // private Long id;
    private String application;
    private String profile;
    private String label;

}

