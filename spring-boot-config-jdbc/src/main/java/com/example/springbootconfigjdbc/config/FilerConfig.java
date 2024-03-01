package com.example.springbootconfigjdbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

import com.example.springbootconfigjdbc.filter.FrontendForwardingFilter;

@Configuration
public class FilerConfig {
    @Bean
    public FilterRegistrationBean<FrontendForwardingFilter> loggingFilter() {
        FilterRegistrationBean<FrontendForwardingFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new FrontendForwardingFilter());

        registrationBean.addUrlPatterns("/camel/*");
        registrationBean.setOrder(999);
        return registrationBean;
    }
}
