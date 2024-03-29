package com.bpwizard.configjdbc.core.exception.controller;


import java.util.List;
import java.util.Map;

import com.bpwizard.configjdbc.core.exception.SpringErrorAttributes;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Used for handling exceptions that can't be handled by
 * <code>DefaultExceptionHandlerControllerAdvice</code>,
 * e.g. exceptions thrown in filters.
 */
public class SpringErrorController extends BasicErrorController {

    private static final Logger logger = LoggerFactory.getLogger(SpringErrorController.class);

    public SpringErrorController(ErrorAttributes errorAttributes,
                                 ServerProperties serverProperties,
                                 List<ErrorViewResolver> errorViewResolvers) {

        super(errorAttributes, serverProperties.getError(), errorViewResolvers);
        logger.info("Created");
    }

    /**
     * Overrides the base method to add our custom logic
     */
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {

        Map<String, Object> body = getErrorAttributes(request,
                getErrorAttributeOptions(request, MediaType.ALL));
        // isIncludeStackTrace(request, MediaType.ALL));

        // if a status was put in SpringErrorAttributes, fetch that
        Object statusObj = body.get(SpringErrorAttributes.HTTP_STATUS_KEY);

        HttpStatus status;

        if (statusObj == null)             // if not put,
            status = getStatus(request);   // let the superclass make the status
        else {
            status = HttpStatus.valueOf((Integer) body.get(SpringErrorAttributes.HTTP_STATUS_KEY));
            body.remove(SpringErrorAttributes.HTTP_STATUS_KEY); // clean the status from the map
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<Map<String, Object>>(body, headers, status);
    }
}
