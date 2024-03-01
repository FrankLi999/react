package com.example.camel.dashboard.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
@Order(9999)
public class FrontendForwardingFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, 
      WebFilterChain webFilterChain) {
        
        // exchange.getResponse()
        //   .getHeaders().add("web-filter", "web-filter-test");
        return webFilterChain.filter(exchange);
    }
}
