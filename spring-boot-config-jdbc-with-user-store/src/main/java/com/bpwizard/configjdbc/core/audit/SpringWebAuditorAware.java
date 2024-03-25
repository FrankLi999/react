package com.bpwizard.configjdbc.core.audit;


import java.io.Serializable;

import com.bpwizard.configjdbc.core.security.model.UserDto;
import com.bpwizard.configjdbc.core.web.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Needed for auto-filling of the
 * AbstractAuditable columns of AbstractUser
 */
public class SpringWebAuditorAware<ID extends Serializable>
        extends AbstractAuditorAware<ID> {

    private static final Logger logger = LoggerFactory.getLogger(SpringWebAuditorAware.class);

    public SpringWebAuditorAware() {
        logger.info("Created");
    }

    @Override
    protected UserDto currentUser() {
        return WebUtils.currentUser();
    }
}
