package com.example.camel.dashboard.config.reactive;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
// import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.net.URI;

import javax.sql.DataSource;   
@Configuration
// @Import(DataSourceAutoConfiguration.class)
// @EnableTransactionManagement
// @EnableJdbcRepositories(basePackages="{a.b.c}")
// @EnableJpaRepositories(basePackages="{a.b.c}")
// @EnableR2dbcRepositories(basePackages="{com.example.camel.dashboard.repository.r2dbc}")

public class RouterConfiguration implements WebFluxConfigurer, EnvironmentAware {

    private Environment env;

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }
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

    // @Bean
    // public DataSource dataSource() {
    //     return new DriverManagerDataSource(
    //         env.getProperty("spring.datasource.url"),
    //         env.getProperty("spring.datasource.username"),
    //         env.getProperty("spring.datasource.password")
    //     );
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
