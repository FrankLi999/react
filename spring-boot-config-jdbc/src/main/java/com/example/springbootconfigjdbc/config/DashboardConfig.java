package com.example.springbootconfigjdbc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DashboardConfig implements WebMvcConfigurer {
    @Override
   public void addResourceHandlers(final ResourceHandlerRegistry registry) {
       // @formatter:off
       // S2i Integrator React static resources
       System.out.println(">>>>>>>>>>>>addResourceHandlers >>>>>>>>>>>>");
       registry
               .addResourceHandler("/camel/static/**")
               .addResourceLocations("classpath:/my-camel-static/static/");
       registry
               .addResourceHandler("/camel/static/img/**")
               .addResourceLocations("classpath:/my-camel-static/img/");
       registry
               .addResourceHandler("/camel/*")
               .addResourceLocations("classpath:/my-camel-static/");

       // @formatter:on
   }
}
