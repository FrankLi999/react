package com.bpwizard.configjdbc.core.validation;


import jakarta.validation.Constraint;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Captcha validation constraint annotation
 *
 * Reference
 *   http://www.captaindebug.com/2011/07/writng-jsr-303-custom-constraint_26.html#.VIVhqjGUd8E
 *   http://www.captechconsulting.com/blog/jens-alm/versioned-validated-and-secured-rest-services-spring-40-2?_ga=1.71504976.2113127005.1416833905
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=CaptchaValidator.class)
public @interface Captcha {

    String message() default "{com.bpwizard.spring.wrong.captcha}";
    Class[] groups() default {};

    Class[] payload() default {};
}