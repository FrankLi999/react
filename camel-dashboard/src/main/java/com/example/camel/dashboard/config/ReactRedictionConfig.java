package com.example.camel.dashboard.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.net.URI;   
// @Configuration
public class ReactRedictionConfig {
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
}
