package com.example.springbootconfigjdbc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
// import lombok.extern.jackson.Jacksonized;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// @Jacksonized
public class ConfigurationProperty {
   // private Long id;
   private String propKey; 
   private String propValue;
}
