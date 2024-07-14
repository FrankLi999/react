package io.hawt.example.spring.boot.camel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshConfigRequest {
    private RequestHeader requestHeader;
    private List<RefreshConfig> refreshConfigurations;
}
