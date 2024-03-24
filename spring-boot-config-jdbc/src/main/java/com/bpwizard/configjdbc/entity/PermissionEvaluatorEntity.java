package com.bpwizard.configjdbc.entity;

import com.bpwizard.configjdbc.security.model.UserDto;

public interface PermissionEvaluatorEntity {

    /**
     * Whether the given user has the given permission for
     * this entity. Override this method where you need.
     */
    public boolean hasPermission(UserDto currentUser, String permission);
}
