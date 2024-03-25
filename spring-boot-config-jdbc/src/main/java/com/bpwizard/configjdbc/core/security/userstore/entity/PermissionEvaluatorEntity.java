package com.bpwizard.configjdbc.core.security.userstore.entity;

import com.bpwizard.configjdbc.core.security.model.UserDto;

public interface PermissionEvaluatorEntity {

    /**
     * Whether the given user has the given permission for
     * this entity. Override this method where you need.
     */
    public boolean hasPermission(UserDto currentUser, String permission);
}
