package mycamel.spring.boot.camel.routes.config;

import java.time.Duration;

import feign.Logger;
import feign.slf4j.Slf4jLogger;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mycamel.spring.boot.camel.support.FeignApiRequestInterceptor;
import mycamel.spring.boot.camel.support.HttpClientUtils;

@Configuration
public class FeignClientConfig {

	@Value("${my-camel.webclient.connection-timeout-millis:0}")
	private long connectionTineout;

	@Value("${my-camel.webclient.read-timeout-millis:0}")
	private long readTineout;

	@Value("${my-camel.webclient.mac-connection:300}")
	private int maxConnection;

	@Value("${my-camel.feign.logger.level:NONE}")
	private Logger.Level feignLogLevel;

	@Bean("myCamelApiClientCircuitBreakerConfig")
	@ConfigurationProperties(prefix = "my-camel.circuitbreaker.api-client")
	public MyCamelCircuitBreakerConfig myCamelApiClientCircuitBreakerConfig() {
		return new MyCamelCircuitBreakerConfig();
	}

	@Bean("apiClientCircuitBreakerConfig")
	public CircuitBreakerConfig apiClientCircuitBreakerConfig(
			@Qualifier("myCamelApiClientCircuitBreakerConfig") MyCamelCircuitBreakerConfig myCamelCircuitBreakerConfig) {
		return CircuitBreakerConfig.custom()
			.slidingWindowType(
					CircuitBreakerConfig.SlidingWindowType.valueOf(myCamelCircuitBreakerConfig.getSlidingWindowType()))
			.slidingWindowSize(myCamelCircuitBreakerConfig.getSlidingWindowSize())
			.recordException(new ResponseExceptionPredicate())
			.waitDurationInOpenState(Duration.ofSeconds(myCamelCircuitBreakerConfig.getWaitDurationInOpenState()))
			.failureRateThreshold(myCamelApiClientCircuitBreakerConfig().getFailureRateThreshold())
			.slowCallRateThreshold(myCamelApiClientCircuitBreakerConfig().getSlowCallRateThreshold())
			.slowCallDurationThreshold(Duration.ofSeconds(myCamelCircuitBreakerConfig.getSlowCallDurationThreshold()))
			.permittedNumberOfCallsInHalfOpenState(
					myCamelApiClientCircuitBreakerConfig().getPermittedNumberOfCallsInHalfOpenState())
			.build();
	}

	@Bean("myCamelRefreshConfigClient")
	public RefreshConfigClient refreshConfigClient(
			@Qualifier("apiClientCircuitBreakerConfig") CircuitBreakerConfig circuitBreakerConfig) {
		CircuitBreaker circuitBreaker = CircuitBreaker.of("my-camel-refresh-config-client", circuitBreakerConfig);
		FeignDecorators decorators = FeignDecorators.builder().withCircuitBreaker(circuitBreaker).build();
		return (connectionTineout > 0)
				? Resilience4jFeign.builder(decorators)
					.requestInterceptor(new FeignApiRequestInterceptor())
					.logLevel(feignLogLevel)
					.logger(new Slf4jLogger(RefreshConfigClient.class))
					.client(HttpClientUtils.httpClient(maxConnection, connectionTineout, readTineout))
					.target(RefreshConfigClient.class, "http://localhost:8080/actuator/refresh")
				: Resilience4jFeign.builder(decorators)
					.requestInterceptor(new FeignApiRequestInterceptor())
					.logLevel(feignLogLevel)
					.logger(new Slf4jLogger(RefreshConfigClient.class))
					.client(HttpClientUtils.httpClient(maxConnection))
					.target(RefreshConfigClient.class, "http://localhost:8080/actuator/refresh");

	}

}
