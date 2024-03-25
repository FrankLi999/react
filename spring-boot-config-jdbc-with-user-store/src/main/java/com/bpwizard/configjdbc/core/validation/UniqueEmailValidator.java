package com.bpwizard.configjdbc.core.validation;

import com.bpwizard.configjdbc.core.security.userstore.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validator for unique-email
 */
public class UniqueEmailValidator
        implements ConstraintValidator<UniqueEmail, String> {

    private static final Logger logger = LoggerFactory.getLogger(UniqueEmailValidator.class);

     private UserService<?, ?> userService;

    public UniqueEmailValidator(UserService<?, ?> userService) {

        this.userService = userService;
        logger.info("Created");
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {

        logger.debug("Validating whether email is unique: " + email);
        // return !userService.findByEmail(email).isPresent();
        return "config@admin.com".equals(email);
    }
}
