package io.hawt.example.spring.boot.camel.routes.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.feign.FeignDecorators;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class FeignClientConfig {
    @Bean("myCamelApiClientCircuitBreakerConfig")
    @ConfigurationProperties(prefix="my-camel.circuitbreaker.api-client")
    public MyCamelCircuitBreakerConfig myCamelApiClientCircuitBreakerConfig() {
        return new MyCamelCircuitBreakerConfig();
    }

    @Bean("apiClientCircuitBreakerConfig")
    public CircuitBreakerConfig apiClientCircuitBreakerConfig(@Qualifier("myCamelApiClientCircuitBreakerConfig") MyCamelCircuitBreakerConfig myCamelCircuitBreakerConfig) {
        return CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.valueOf(myCamelCircuitBreakerConfig.getSlidingWindowType()))
                .slidingWindowSize(myCamelCircuitBreakerConfig.getSlidingWindowSize())
                .recordException(new ResponseExceptionPredicate())
                .waitDurationInOpenState(Duration.ofSeconds(myCamelCircuitBreakerConfig.getWaitDurationInOpenState()))
                .failureRateThreshold(myCamelApiClientCircuitBreakerConfig().getFailureRateThreshold())
                .slowCallRateThreshold(myCamelApiClientCircuitBreakerConfig().getSlowCallRateThreshold())
                .slowCallDurationThreshold(Duration.ofSeconds(myCamelCircuitBreakerConfig.getSlowCallDurationThreshold()))
                .permittedNumberOfCallsInHalfOpenState(myCamelApiClientCircuitBreakerConfig().getPermittedNumberOfCallsInHalfOpenState())
                .build();
    }

    @Bean("myCamelRefreshConfigClient")
    public RefreshConfigClient refreshCOnfigClient(@Qualifier("apiClientCircuitBreakerConfig") CircuitBreakerConfig circuitBreakerConfig) {
        CircuitBreaker circuitBreaker = CircuitBreaker.of("refresh-config-client", circuitBreakerConfig);
        FeignDecorators decorators = FeignDecorators.builder()
                .withCircuitBreaker(circuitBreaker)
                .build();
        return null;
    }
}
