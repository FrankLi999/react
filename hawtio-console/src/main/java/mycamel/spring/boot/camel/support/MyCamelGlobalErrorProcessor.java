package mycamel.spring.boot.camel.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.camel.CamelExchangeException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import mycamel.spring.boot.camel.dto.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class MyCamelGlobalErrorProcessor implements Processor {

	private final ExceptionUtils exceptionUtils;

	private final CamelUtils camelUtils;

	private final ObjectMapper objectMapper;

	@Override
	public void process(Exchange exchange) throws JsonProcessingException {
		Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
		if (throwable instanceof CamelExchangeException) {
			throwable = throwable.getCause();
		}
		log.error("MyCamelGlobalErrorProcessor", throwable);
		Map requestContextMap = exchange.getProperty(MyCamelConstants.PROPERTY_MY_CAMEL_CONTEXT_MAP, Map.class);
		RequestContext requestContext = (RequestContext) requestContextMap
			.get(MyCamelConstants.PROPERTY_MY_CAMEL_CONTEXT_MAP);
		String endpoint = requestContext.getEndpointId();
		int httpStatus = exceptionUtils.httpStatus(throwable);
		List<ResponseMessage> responseMessages = new ArrayList<>();
		responseMessages.add(exceptionUtils.apiError(endpoint, throwable, httpStatus));
		if (CollectionUtils.isEmpty(requestContext.getResponseMessages())) {
			responseMessages.addAll(requestContext.getResponseMessages());
		}
		ApiError apiError = ApiError.builder()
			.responseHeader(
					ResponseHeader.builder().responseCode(Boolean.FALSE).responseMessages(responseMessages).build())
			.traceId(requestContext.getRequestHeader().getTraceId())
			.build();
		requestContext.setStatus(HttpStatus.valueOf(httpStatus));
		exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, httpStatus);
		exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		CamelResponse camelResponse = CamelResponse.builder()
			.responseBody(objectMapper.writeValueAsString(apiError))
			.requestContext(requestContext)
			.build();
		exchange.getMessage().setBody(camelResponse);
		exchange.removeProperty(MyCamelConstants.PROPERTY_MY_CAMEL_CONTEXT_MAP);
		camelUtils.removeRequestContext();
	}

}
