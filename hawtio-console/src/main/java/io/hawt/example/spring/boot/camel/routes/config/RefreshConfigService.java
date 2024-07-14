package io.hawt.example.spring.boot.camel.routes.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor()
public class RefreshConfigService {
    // @Qualifier("myCamelRefreshConfigClient")
    private final  RefreshConfigClient myCamelRefreshConfigClient;
    private final ObjectMapper objectMapper;
    public Set<String> refresh(String refreshConfigUrl) throws JsonProcessingException {
        return objectMapper.readValue(
                this.myCamelRefreshConfigClient.refresh(URI.create(refreshConfigUrl)),
                objectMapper.getTypeFactory().constructCollectionType(Set.class, String.class));
    }
}
