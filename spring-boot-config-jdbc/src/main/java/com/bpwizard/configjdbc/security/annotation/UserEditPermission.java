package com.bpwizard.configjdbc.security.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PreAuthorize;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasPermission(#user, 'edit')")
public @interface UserEditPermission {}