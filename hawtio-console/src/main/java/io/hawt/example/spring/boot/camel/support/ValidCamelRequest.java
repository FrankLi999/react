package io.hawt.example.spring.boot.camel.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCamelRequest {
    String logSubject()  default "My Camel: API_Request (Enter)";
    Class<?> type();
    String endpoint() default "MyCamel";

}
