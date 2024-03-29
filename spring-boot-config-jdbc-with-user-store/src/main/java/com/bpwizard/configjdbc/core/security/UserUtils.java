package com.bpwizard.configjdbc.core.security;


import com.bpwizard.configjdbc.core.security.model.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserUtils {

    private static final Logger logger = LoggerFactory.getLogger(UserUtils.class);

    public static final int EMAIL_MIN = 4;
    public static final int EMAIL_MAX = 250;
    public static final int UUID_LENGTH = 36;
    public static final int PASSWORD_MAX = 50;
    public static final int PASSWORD_MIN = 6;

    /**
     * Role constants. To allow extensibility, this couldn't be made an enum
     */
    public interface Role {

        static final String UNVERIFIED = "UNVERIFIED";
        static final String BLOCKED = "BLOCKED";
        static final String ADMIN = "admin";
        static final String USER = "user";
        static final String READWRITE = "readwrite";
        static final String READONLY = "readonly";
    }

    public interface Permission {

        static final String EDIT = "edit";
    }

    // validation groups
    public interface SignUpValidation {
    }

    public interface UpdateValidation {
    }

    public interface ChangeEmailValidation {
    }

    // JsonView for Sign up
    public interface SignupInput {
    }

    public static <ID> boolean hasPermission(ID id, UserDto currentUser, String permission) {

        logger.debug("Computing " + permission + " permission for User " + id + "\n  Logged in user: " + currentUser);

        if (permission.equals("edit")) {

            if (currentUser == null)
                return false;

            boolean isSelf = currentUser.getId().equals(id.toString());
            return isSelf || currentUser.isGoodAdmin(); // self or admin;
        }

        return false;
    }
}
