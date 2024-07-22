package mycamel.spring.boot.camel.routes;

import lombok.RequiredArgsConstructor;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mycamel.spring.boot.camel.routes.bean.RefreshConfigAggregationStrategy;
import mycamel.spring.boot.camel.routes.bean.RefreshConfigProcessor;
import mycamel.spring.boot.camel.support.MyCamelGlobalErrorProcessor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RefreshConfigRouter extends RouteBuilder {

	private final MyCamelGlobalErrorProcessor globalErrorProcessor;

	private final RefreshConfigProcessor refreshConfigProcessor;

	private final RefreshConfigAggregationStrategy refreshConfigAggregationStrategy;

	@Override
	public void configure() throws Exception {
		onException(Throwable.class).handled(true).process(globalErrorProcessor);
		from("direct:refresh-config").id("MY_CAMEL_REFRESH_CONFIG")
			.process(refreshConfigProcessor::setupRequestContext)
			.split(method(refreshConfigProcessor, "splitRefreshConfig"), refreshConfigAggregationStrategy)
			.streaming()
			.parallelProcessing(false)
			.process(refreshConfigProcessor::refreshConfig)
			.end()
			.process(refreshConfigProcessor::createApiResponse);
	}

}
