package com.bpwizard.configjdbc.validation;


import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import com.bpwizard.configjdbc.security.UserUtils;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Annotation for password constraint
 *
 * @see <a href="http://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#example-composed-constraint">Composed constraint example</a>
 *
 */
@NotBlank(message="{com.bpwizard.spring.blank.password}")
@Size(min= UserUtils.PASSWORD_MIN, max=UserUtils.PASSWORD_MAX,
        message="{com.bpwizard.spring.invalid.password.size}")
@Retention(RUNTIME)
@Constraint(validatedBy = { })
public @interface Password {

    String message() default "{com.bpwizard.spring.invalid.password.size}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
