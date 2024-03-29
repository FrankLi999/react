package com.bpwizard.configjdbc.core.exception;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.WebExchangeBindException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Holds a field or form error
 */
@Getter @AllArgsConstructor @ToString
public class SpringFieldError {

    // Name of the field. Null in case of a form level error.
    private String field;

    // Error code. Typically the I18n message-code.
    private String code;

    // Error message
    private String message;

    /**
     * Converts a set of ConstraintViolations
     * to a list of FieldErrors
     *
     * @param constraintViolations
     */
    public static List<SpringFieldError> getErrors(
            Set<ConstraintViolation<?>> constraintViolations) {

        return constraintViolations.stream()
                .map(SpringFieldError::of).collect(Collectors.toList());
    }


    /**
     * Converts a ConstraintViolation
     * to a FieldError
     */
    private static SpringFieldError of(ConstraintViolation<?> constraintViolation) {

        // Get the field name by removing the first part of the propertyPath.
        // (The first part would be the service method name)
        String field = StringUtils.substringAfter(
                constraintViolation.getPropertyPath().toString(), ".");

        return new SpringFieldError(field,
                constraintViolation.getMessageTemplate(),
                constraintViolation.getMessage());
    }

    public static List<SpringFieldError> getErrors(WebExchangeBindException ex) {

        List<SpringFieldError> errors = ex.getFieldErrors().stream()
                .map(SpringFieldError::of).collect(Collectors.toList());

        errors.addAll(ex.getGlobalErrors().stream()
                .map(SpringFieldError::of).collect(Collectors.toSet()));

        return errors;
    }

    private static SpringFieldError of(FieldError fieldError) {

        return new SpringFieldError(fieldError.getObjectName() + "." + fieldError.getField(),
                fieldError.getCode(), fieldError.getDefaultMessage());
    }

    public static SpringFieldError of(ObjectError error) {

        return new SpringFieldError(error.getObjectName(),
                error.getCode(), error.getDefaultMessage());
    }

}
