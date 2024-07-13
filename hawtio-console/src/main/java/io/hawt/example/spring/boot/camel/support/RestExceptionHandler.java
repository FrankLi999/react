package io.hawt.example.spring.boot.camel.support;

import io.hawt.example.spring.boot.camel.dto.ApiError;
import io.hawt.example.spring.boot.camel.dto.ResponseHeader;
import io.hawt.example.spring.boot.camel.exception.InvalidteRequestException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

	@Value("${my-camel.application.name}")
	private String endpointId;

	private final ExceptionUtils exceptionUtils;

	@ExceptionHandler(Throwable.class)
	protected ResponseEntity<ApiError> handleGenericError(Throwable t) {
		int httpStatusCode = exceptionUtils.httpStatus(t);
		ApiError apiError = ApiError.builder()
			.responseHeader(ResponseHeader.builder().responseCode(Boolean.FALSE).build())
			.build();
		apiError.getResponseHeader().getResponseMessages().add(exceptionUtils.apiError(endpointId, t, httpStatusCode));
		return ResponseEntity.status(httpStatusCode).body(apiError);
	}

	@ExceptionHandler(InvalidteRequestException.class)
	protected ResponseEntity<ApiError> handleInvalidteRequestException(InvalidteRequestException e) {
		log.error("Invalid request error. {}", e.getMessage(), e);
		int httpStatusCode = 400;
		ApiError apiError = ApiError.builder()
			.responseHeader(ResponseHeader.builder().responseCode(Boolean.FALSE).build())
			.build();
		apiError.getResponseHeader()
			.getResponseMessages()
			.add(exceptionUtils.apiError(e.getEndpointId(), e, httpStatusCode));
		return ResponseEntity.badRequest().body(apiError);
	}

}
