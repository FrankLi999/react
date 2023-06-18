package com.example.camel.dashboard.dto;

import lombok.Builder;
import lombok.Data;
// import lombok.extern.jackson.Jacksonized;

@Data
@Builder
// @Jacksonized
public class ConfigurationProperty {
   private String propKey; 
   private String propValue;
}
