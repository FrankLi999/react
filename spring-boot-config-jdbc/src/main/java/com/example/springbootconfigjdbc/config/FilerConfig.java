package com.example.springbootconfigjdbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

import com.example.springbootconfigjdbc.filter.FrontendForwardingFilter;

// @Configuration
public class FilerConfig {
    // @Bean
    // public FilterRegistrationBean<FrontendForwardingFilter> filters() {
    //     FilterRegistrationBean<FrontendForwardingFilter> registrationBean = new FilterRegistrationBean<>();

    //     registrationBean.setFilter(new FrontendForwardingFilter());
    //     // /s2i-integrator/config/spring/admin/api/configurations
    //     registrationBean.addUrlPatterns("/spring/admin/*");
    //     registrationBean.setOrder(999);
    //     return registrationBean;
    // }
}
