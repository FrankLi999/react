package mycamel.spring.boot.camel.routes.config;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MyCamelCircuitBreakerConfig {

	private Boolean registerHealthIndicator;

	private String slidingWindowType;

	private int slidingWindowSize;

	private int failureRateThreshold;

	private int slowCallRateThreshold;

	private int slowCallDurationThreshold;

	private int minimumNumberOfCalls;

	private int permittedNumberOfCallsInHalfOpenState;

	private int waitDurationInOpenState;

}
