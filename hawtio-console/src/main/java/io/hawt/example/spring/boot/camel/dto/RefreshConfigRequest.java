package io.hawt.example.spring.boot.camel.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
@Builder
public class RefreshConfigRequest {
    private Set<RefreshConfig> refreshConfigurations;
}
