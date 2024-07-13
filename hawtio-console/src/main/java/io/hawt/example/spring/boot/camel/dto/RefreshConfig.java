package io.hawt.example.spring.boot.camel.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class RefreshConfig {
    private String applicationName;
    private String refreshUrl;
}
