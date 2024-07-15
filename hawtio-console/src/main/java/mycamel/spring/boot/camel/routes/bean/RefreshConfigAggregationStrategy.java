package mycamel.spring.boot.camel.routes.bean;

import mycamel.spring.boot.camel.dto.CamelResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class RefreshConfigAggregationStrategy implements AggregationStrategy {
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Exchange exchange = (oldExchange == null) ? newExchange : oldExchange;
        Object response = newExchange.getMessage().getBody(Object.class);
        if (response instanceof CamelResponse) {
            exchange.getMessage().setBody(response);
        } else {
            Map<String, Set<String>> itemResponse = (Map<String, Set<String>>) response;
            Map<String, Set<String>> finalResponse = (oldExchange == null) ? new LinkedHashMap<>() : oldExchange.getMessage().getBody(Map.class);
            finalResponse.putAll(itemResponse);
            exchange.getMessage().setBody(finalResponse);
        }
        return exchange;
    }
}
