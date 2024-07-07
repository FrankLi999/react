package com.example.camel.dashboard.config.reactive;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.net.URI;   
@Configuration
public class RouterConfiguration implements WebFluxConfigurer {
    // @Bean
    // RouterFunction<ServerResponse> reactProtectedSiteRedirection() {
    //     return  route(GET("/integrator/**"), req -> {
    //         System.out.println(">>>>>>>>>>>>>>> 1");
    //         return ServerResponse.temporaryRedirect(URI.create("/"))
    //                     .build();
    //                 });
    // }

    // @Bean
    // RouterFunction<ServerResponse> reactPublicSiteRedirection() {
        
    //     return  route(GET("/public/**"), req -> {
    //         System.out.println(">>>>>>>>>>>>>>> 2");
    //         return ServerResponse.temporaryRedirect(URI.create("/"))
    //                     .build();
    //         });
        
    // }
    
    @Bean
    RouterFunction<ServerResponse> swiIntegratorRouterfunction() {
        return  route(GET("/my/camel"), (ServerRequest req) -> ServerResponse.temporaryRedirect(URI.create("/my/camel/index.html")).build());
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        // @formatter:off
        // Hawtio React static resources
        registry
            .addResourceHandler("/my/camel/static/**")
            .addResourceLocations("classpath:/my-camel-static/static/");
        registry
            .addResourceHandler("/my/camel/img/**")
            .addResourceLocations("classpath:/my-camel-static/img/");
        registry
            .addResourceHandler("/my/camel/i18n/**")
            .addResourceLocations("classpath:/static/i18n/");            
        registry
            .addResourceHandler("/my/camel/**")
            .addResourceLocations("classpath:/my-camel-static/");    
            
        // @formatter:on
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                // .allowedMethods("PUT")
                //.maxAge(3600);
                //.allowedOrigins("http://allowed-origin.com");
                .allowedOrigins("*");
    }
}
