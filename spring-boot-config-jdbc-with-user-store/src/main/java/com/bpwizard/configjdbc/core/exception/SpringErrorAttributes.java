package com.bpwizard.configjdbc.core.exception;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * Used for handling exceptions that can't be handled by
 * <code>DefaultExceptionHandlerControllerAdvice</code>,
 * e.g. exceptions thrown in filters.
 */
public class SpringErrorAttributes<T extends Throwable> extends DefaultErrorAttributes {

    private static final Logger logger = LoggerFactory.getLogger(SpringErrorAttributes.class);

    public static final String HTTP_STATUS_KEY = "httpStatus";

    private ErrorResponseComposer<T> errorResponseComposer;

    public SpringErrorAttributes(ErrorResponseComposer<T> errorResponseComposer) {

        this.errorResponseComposer = errorResponseComposer;
        logger.info("Created");
    }

    /**
     * Calls the base class and then adds our details
     */
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest request,
                                                  ErrorAttributeOptions options) {
        // boolean includeStackTrace) {

        Map<String, Object> errorAttributes =
                super.getErrorAttributes(request, options);
        // super.getErrorAttributes(request, includeStackTrace);

        addErrorDetails(errorAttributes, request);

        return errorAttributes;
    }

    /**
     * Handles exceptions
     */
    @SuppressWarnings("unchecked")
    protected void addErrorDetails(
            Map<String, Object> errorAttributes, WebRequest request) {

        Throwable ex = getError(request);

        errorResponseComposer.compose((T)ex).ifPresent(errorResponse -> {

            // check for null - errorResponse may have left something for the DefaultErrorAttributes

            if (errorResponse.getExceptionId() != null)
                errorAttributes.put("exceptionId", errorResponse.getExceptionId());

            if (errorResponse.getMessage() != null)
                errorAttributes.put("message", errorResponse.getMessage());

            if (errorResponse.getErrorCode() != null)
                errorAttributes.put("errorCode", errorResponse.getErrorCode());
            if (errorResponse.getArguments() != null)
                errorAttributes.put("arguments", errorResponse.getArguments());
            Integer status = errorResponse.getStatus();

            if (status != null) {
                errorAttributes.put(HTTP_STATUS_KEY, status); // a way to pass response status to SpringErrorController
                errorAttributes.put("status", status);
                errorAttributes.put("reasonPhrase", errorResponse.getReasonPhrase());
            }

            if (errorResponse.getErrors() != null)
                errorAttributes.put("errors", errorResponse.getErrors());
        });

        if (errorAttributes.get("exceptionId") == null)
            errorAttributes.put("exceptionId", SpringExceptionUtils.getExceptionId(ex));
    }
}
