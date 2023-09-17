package com.example.camel.dashboard.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
//@Jacksonized
public class ApplicationProfile {
    private String application;
    private String profile;
}