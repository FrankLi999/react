package com.bpwizard.configjdbc.core.exception;


import java.util.function.Supplier;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Useful helper methods
 */
public class SpringExceptionUtils {

    private static final Logger logger = LoggerFactory.getLogger(SpringExceptionUtils.class);

    private static MessageSource messageSource;
    private static LocalValidatorFactoryBean validator;
    private static ExceptionIdMaker exceptionIdMaker;

    public static final MultiErrorException NOT_FOUND_EXCEPTION = new MultiErrorException();

    /**
     * Constructor
     */
    public SpringExceptionUtils(MessageSource messageSource,
                                LocalValidatorFactoryBean validator,
                                ExceptionIdMaker exceptionIdMaker) {

        SpringExceptionUtils.messageSource = messageSource;
        SpringExceptionUtils.validator = validator;
        SpringExceptionUtils.exceptionIdMaker = exceptionIdMaker;

        logger.info("Created");
    }


    @PostConstruct
    public void postConstruct() {

        NOT_FOUND_EXCEPTION
                .httpStatus(HttpStatus.NOT_FOUND)
                .validate(false, "com.bpwizard.spring.notFound");

        logger.info("NOT_FOUND_EXCEPTION built");
    }


    /**
     * Gets a message from messages.properties
     */
    public static String getMessage(String messageKey, Object... args) {

        if (messageSource == null)
            return "ApplicationContext unavailable, probably unit test going on";

        // http://stackoverflow.com/questions/10792551/how-to-obtain-a-current-user-locale-from-spring-without-passing-it-as-a-paramete
        return messageSource.getMessage(messageKey, args,
                LocaleContextHolder.getLocale());
    }


    /**
     * Creates a MultiErrorException out of the given parameters
     */
    public static MultiErrorException validate(
            boolean valid, String messageKey, Object... args) {

        return validateField(null, valid, messageKey, args);
    }


    /**
     * Creates a MultiErrorException out of the given parameters
     */
    public static MultiErrorException validateField(
            String fieldName, boolean valid, String messageKey, Object... args) {

        return new MultiErrorException().validateField(fieldName, valid, messageKey, args);
    }


    /**
     * Creates a MultiErrorException out of the constraint violations in the given bean
     */
    public static <T> MultiErrorException validateBean(String beanName, T bean, Class<?>... validationGroups) {

        return new MultiErrorException()
                .exceptionId(getExceptionId(new ConstraintViolationException(null)))
                .validationGroups(validationGroups)
                .validateBean(beanName, bean);
    }


    /**
     * Throws 404 Error is the entity isn't found
     */
    public static <T> void ensureFound(T entity) {

        SpringExceptionUtils.validate(entity != null,
                        "com.bpwizard.spring.notFound")
                .httpStatus(HttpStatus.NOT_FOUND).go();
    }


    /**
     * Supplys a 404 exception
     */
    public static Supplier<MultiErrorException> notFoundSupplier() {

        return () -> NOT_FOUND_EXCEPTION;
    }


    public static String getExceptionId(Throwable ex) {

        Throwable root = getRootException(ex);
        return exceptionIdMaker.make(root);
    }


    private static Throwable getRootException(Throwable ex) {

        if (ex == null) return null;

        while(ex.getCause() != null)
            ex = ex.getCause();

        return ex;
    }


    public static LocalValidatorFactoryBean validator() {
        return validator;
    }
}
