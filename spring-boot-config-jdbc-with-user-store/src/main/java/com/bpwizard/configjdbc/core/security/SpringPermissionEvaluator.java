package com.bpwizard.configjdbc.core.security;


import java.io.Serializable;

import com.bpwizard.configjdbc.core.security.userstore.entity.PermissionEvaluatorEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

/**
 * Needed to check the permission for the service methods
 * annotated with @PreAuthorize("hasPermission(...
 */
public class SpringPermissionEvaluator implements PermissionEvaluator {

    private static final Logger logger = LoggerFactory.getLogger(SpringPermissionEvaluator.class);

    public SpringPermissionEvaluator() {
        logger.info("Created");
    }

    /**
     * Called by Spring Security to evaluate the permission
     *
     * @param auth	Spring Security authentication object,
     * 				from which the current-user can be found
     * @param targetDomainObject	Object for which permission is being checked
     * @param permission			What permission is being checked for, e.g. 'edit'
     */
    @Override
    public boolean hasPermission(Authentication auth,
                                 Object targetDomainObject, Object permission) {

        logger.debug("Checking whether " + auth
                + "\n  has " + permission + " permission for "
                + targetDomainObject);

        if (targetDomainObject == null)	// if no domain object is provided,
            return true;				// let's pass, allowing the service method
        // to throw a more sensible error message

        // Let's delegate to the entity's hasPermission method
        PermissionEvaluatorEntity entity = (PermissionEvaluatorEntity) targetDomainObject;
        return entity.hasPermission(SecurityUtils.currentUser(auth), (String) permission);
    }


    /**
     * We need to override this method as well. To keep things simple,
     * Let's not use this, throwing exception is someone uses it.
     */
    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId, String targetType, Object permission) {

        throw new UnsupportedOperationException("hasPermission() by ID is not supported");
    }

}
