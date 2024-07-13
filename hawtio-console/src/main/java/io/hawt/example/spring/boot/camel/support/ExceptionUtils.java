package io.hawt.example.spring.boot.camel.support;

import jakarta.validation.ConstraintViolationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;

import io.hawt.example.spring.boot.camel.dto.ApiError;
import io.hawt.example.spring.boot.camel.dto.ResponseMessage;
import io.hawt.example.spring.boot.camel.exception.BadCredentialsException;
import io.hawt.example.spring.boot.camel.exception.InvalidteRequestException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.ErrorResponse;
import org.springframework.web.client.RestClientResponseException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionUtils {

	private final ObjectMapper objectMapper;

	public String messageCodePrefix() {
		return "MyCamel";
	}

	public int httpStatus(Throwable throwable) {
		int httpStatus = 500;
		if (throwable instanceof ErrorResponse) {
			httpStatus = ((ErrorResponse) throwable).getStatusCode().value();
		}
		else if (throwable instanceof RestClientResponseException) {
			httpStatus = ((RestClientResponseException) throwable).getStatusCode().value();
		}
		else if (throwable instanceof InvalidteRequestException || throwable instanceof ConstraintViolationException) {
			httpStatus = 400;
		}
		else if (throwable instanceof java.net.SocketTimeoutException) {
			httpStatus = 504;
		}
		else if (throwable instanceof FeignException && ((FeignException) throwable).status() >= 400) {
			httpStatus = ((FeignException) throwable).status();
		}
		else if (throwable instanceof BadCredentialsException) {
			httpStatus = 401;
		}
		return httpStatus;
	}

	public ResponseMessage apiError(String endpointId, Throwable throwable) {
		return apiError(endpointId, throwable, httpStatus(throwable));
	}

	public ResponseMessage apiError(String endpointId, Throwable throwable, int httpStaus) {
		String messageCode = "";
		String logMessage = "";

		if (throwable instanceof FeignException) {
			FeignException fe = (FeignException) throwable;
			if (fe.responseBody().isPresent()) {
				String errorResp = fe.contentUTF8();
				errorResp = (errorResp == null) ? "" : errorResp;
				try {
					ApiError apiError = this.objectMapper.readValue(errorResp, ApiError.class);
					logMessage = apiError.getResponseHeader().getResponseMessages().get(0).getLogMessage();
					messageCode = apiError.getResponseHeader().getResponseMessages().get(0).getMessageCode();
				}
				catch (JsonProcessingException e) {
					// Do nothing
				}
			}
			if (StringUtils.isEmpty(messageCode)) {
				messageCode = String.format("%s%s_%d", messageCodePrefix(), endpointId, httpStaus);
				if (fe.status() < 400 && fe.getCause() != null) {
					logMessage = String.format("Error Type - %s, Error Details: 5s.",
							fe.getCause().getClass().getName(), fe.getCause().getMessage());
				}
				else {
					logMessage = String.format("Error Type - %s, Error Details: 5s.",
							fe.getCause().getClass().getName(),
							fe.responseBody().isPresent() ? fe.contentUTF8() : throwable.getMessage());
				}
			}
		}
		else if (throwable instanceof RestClientResponseException) {
			try {
				ApiError apiError = ((RestClientResponseException) throwable).getResponseBodyAs(ApiError.class);
				if (apiError != null && apiError.getResponseHeader() != null
						&& !ObjectUtils.isEmpty(apiError.getResponseHeader().getResponseMessages())) {
					logMessage = apiError.getResponseHeader().getResponseMessages().get(0).getLogMessage();
					messageCode = apiError.getResponseHeader().getResponseMessages().get(0).getMessageCode();
				}
			}
			catch (Exception e) {
				log.warn("Failed to process " + throwable.getClass().getName(), e);
			}
		}

		if (!StringUtils.hasText(messageCode)) {
			messageCode = String.format("%s%s_%d", messageCodePrefix(), endpointId, httpStaus);
			logMessage = StringUtils.hasText(throwable.getMessage()) ? throwable.getMessage()
					: throwable.getClass().getName();
		}

		return ResponseMessage.builder()
			.messageCode(messageCode)
			.severity(ResponseMessage.Severity.E)
			.logMessage(logMessage)
			.build();
	}

	public ResponseMessage soapError(String endpointId, Throwable throwable, int httpStaus) {
		String messageCode = String.format("%s%s_%d", messageCodePrefix(), endpointId, httpStaus);
		String logMessage = "";
		if (throwable instanceof FeignException) {
			FeignException fe = (FeignException) throwable;
			if (fe.status() < 400 && fe.getCause() != null) {
				logMessage = String.format("Error Type - %s, Error Details: 5s.", fe.getCause().getClass().getName(),
						fe.getCause().getMessage());
			}
			else {
				logMessage = String.format("Error Type - %s, Error Details: 5s.", fe.getCause().getClass().getName(),
						fe.responseBody().isPresent() ? fe.contentUTF8() : throwable.getMessage());
			}
		}
		else {

			logMessage = String.format("Error Type - %s, Error Details: 5s.", throwable.getClass().getName(),
					throwable.getMessage());
		}
		return ResponseMessage.builder()
			.messageCode(messageCode)
			.severity(ResponseMessage.Severity.E)
			.logMessage(logMessage)
			.build();
	}

}
