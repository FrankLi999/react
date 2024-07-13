package io.hawt.example.spring.boot.camel.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CamelRequestResolverConfiguration implements WebMvcConfigurer {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CamelRequestArgumentResolverService camelRequestArgumentResolverService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CamelRequestArgumentResolver(objectMapper, camelRequestArgumentResolverService));
    }
}
