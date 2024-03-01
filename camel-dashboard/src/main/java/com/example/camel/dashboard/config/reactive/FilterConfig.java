package com.example.camel.dashboard.config.reactive;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.camel.dashboard.filter.FrontendForwardingFilter;

@Configuration
public class FilterConfig {
    private final String DASHBOARD_URL_PATTERN = "/my/camel/*";
    @Bean
    public FilterRegistrationBean<FrontendForwardingFilter> requestIdFilter() {
        FilterRegistrationBean<FrontendForwardingFilter> filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new RequestIdFilter());
        filterRegistrationBean.addUrlPatterns(DASHBOARD_URL_PATTERN);
        return filterRegistrationBean;
    }
}
