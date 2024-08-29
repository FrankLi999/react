package mycamel.spring.boot.hawtio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        // @formatter:off
        registry
                .addResourceHandler("/web/i18next/**")
                .addResourceLocations("classpath:/static/i18n/");
        // @formatter:on
    }
}