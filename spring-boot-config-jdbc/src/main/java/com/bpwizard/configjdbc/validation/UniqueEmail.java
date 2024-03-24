package com.bpwizard.configjdbc.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.bpwizard.configjdbc.security.UserUtils;
import jakarta.validation.Constraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Annotation for unique-email constraint,
 * ensuring that the given email id is not already
 * used by a user.
 */
@NotBlank(message = "{com.bpwizard.spring.blank.email}")
@Size(min= UserUtils.EMAIL_MIN, max=UserUtils.EMAIL_MAX,
        message = "{com.bpwizard.spring.invalid.email.size}")
@Email(message = "{com.bpwizard.spring.invalid.email}")
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=UniqueEmailValidator.class)
public @interface UniqueEmail {

    String message() default "{com.bpwizard.spring.duplicate.email}";

    @SuppressWarnings("rawtypes")
    Class[] groups() default {};

    @SuppressWarnings("rawtypes")
    Class[] payload() default {};
}
