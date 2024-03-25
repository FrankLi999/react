-- DROP FUNCTION IF EXISTS UPDATE_UPDATE_AT_COLUMN();
CREATE OR REPLACE FUNCTION UPDATE_ACCOUNT_UPDATE_AT_COLUMN()
RETURNS TRIGGER
AS $$
BEGIN
   NEW.updated_at = now();
RETURN NEW;
END;
$$ language plpgsql;

-- DROP TRIGGER IF EXISTS UPDATE_USR_UPDATE_AT ON usr;
-- DROP TABLE IF EXISTS usr;
CREATE TABLE usr (
                     ID BIGSERIAL PRIMARY KEY,
                     created_by_id bigint DEFAULT NULL,
                     created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     last_modified_by_id bigint DEFAULT NULL,
                     updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
                     version bigint DEFAULT 0,
                     attempts int NOT NULL DEFAULT 0,
                     credentials_updated_millis bigint NOT NULL DEFAULT 0,
                     email varchar(250) NOT NULL UNIQUE,
                     email_verified BOOLEAN DEFAULT NULL,
                     first_name varchar(50) DEFAULT NULL,
                     image_url varchar(255) DEFAULT NULL,
                     last_name varchar(50) DEFAULT NULL,
                     lock_expiration_time timestamp DEFAULT NULL,
                     name varchar(50) NOT NULL,
                     new_email varchar(250) DEFAULT NULL,
                     new_password varchar(255) DEFAULT NULL,
                     password varchar(255) NOT NULL,
                     provider varchar(255) DEFAULT 'local',
                     provider_id varchar(255) DEFAULT NULL,
                     salt varchar(255) DEFAULT NULL
);


CREATE TRIGGER UPDATE_USR_UPDATE_AT
    BEFORE UPDATE
    ON usr
    FOR EACH ROW
    EXECUTE PROCEDURE UPDATE_ACCOUNT_UPDATE_AT_COLUMN();


-- DROP TRIGGER IF EXISTS UPDATE_ROLE_UPDATE_AT ON role;
-- DROP TABLE IF EXISTS role;
CREATE TABLE role (
                      ID BIGSERIAL PRIMARY KEY,
                      created_by_id bigint DEFAULT NULL,
                      created_a timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      last_modified_by_id bigint DEFAULT NULL,
                      updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      version bigint DEFAULT 0,
                      name varchar(50) NOT NULL UNIQUE,
                      type varchar(50) NOT NULL
);

CREATE TRIGGER UPDATE_ROLE_UPDATE_AT
    BEFORE UPDATE
    ON role
    FOR EACH ROW
    EXECUTE PROCEDURE UPDATE_ACCOUNT_UPDATE_AT_COLUMN();



-- DROP TRIGGER IF EXISTS UPDATE_TENANT_UPDATE_AT on tenant;
-- DROP TABLE IF EXISTS tenant;
CREATE TABLE tenant (
                        ID BIGSERIAL PRIMARY KEY,
                        created_by_id bigint DEFAULT NULL,
                        created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        last_modified_by_id bigint DEFAULT NULL,
                        updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        version bigint DEFAULT 0,
                        name varchar(50) NOT NULL UNIQUE
);

CREATE TRIGGER UPDATE_TENANT_UPDATE_AT
    BEFORE UPDATE
    ON tenant
    FOR EACH ROW
    EXECUTE PROCEDURE UPDATE_ACCOUNT_UPDATE_AT_COLUMN();


-- DROP TRIGGER IF EXISTS UPDATE_TENANT_ROLE_UPDATE_AT on tenant_role;
-- DROP TABLE IF EXISTS tenant_role;
CREATE TABLE tenant_role (
                             tenant_id bigint NOT NULL,
                             role_id bigint NOT NULL,
                             created_by_id bigint DEFAULT NULL,
                             created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             last_modified_by_id bigint DEFAULT NULL,
                             updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             version bigint DEFAULT 0,
                             PRIMARY KEY (role_id, tenant_id),
                             CONSTRAINT tenant_id FOREIGN KEY (tenant_id) REFERENCES tenant (id),
                             CONSTRAINT role_id FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TRIGGER UPDATE_TENANT_ROLE_UPDATE_AT
    BEFORE UPDATE
    ON tenant_role
    FOR EACH ROW
    EXECUTE PROCEDURE UPDATE_ACCOUNT_UPDATE_AT_COLUMN();


-- DROP TRIGGER IF EXISTS UPDATE_TENANT_USR_UPDATE_AT on tenant_user;
-- DROP TABLE IF EXISTS tenant_user;
CREATE TABLE tenant_user (
                             user_id bigint NOT NULL,
                             tenant_id bigint NOT NULL,
                             created_by_id bigint DEFAULT NULL,
                             created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             last_modified_by_id bigint DEFAULT NULL,
                             updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             version bigint DEFAULT 0,
                             PRIMARY KEY (user_id, tenant_id),
                             CONSTRAINT tenant_id FOREIGN KEY (tenant_id) REFERENCES tenant (id),
                             CONSTRAINT usr_id FOREIGN KEY (user_id) REFERENCES usr(id)
);

CREATE TRIGGER UPDATE_TENANT_USR_UPDATE_AT
    BEFORE UPDATE
    ON tenant_user
    FOR EACH ROW
    EXECUTE PROCEDURE UPDATE_ACCOUNT_UPDATE_AT_COLUMN();


-- DROP TRIGGER IF EXISTS UPDATE_ROLE_USR_UPDATE_AT on role_user;
-- DROP TABLE IF EXISTS role_user;
CREATE TABLE role_user (
                           user_id bigint NOT NULL,
                           role_id bigint NOT NULL,
                           created_by_id bigint DEFAULT NULL,
                           created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           last_modified_by_id bigint DEFAULT NULL,
                           updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           version bigint DEFAULT 0,
                           PRIMARY KEY (user_id, role_id),
                           CONSTRAINT role_id FOREIGN KEY (role_id) REFERENCES role (id),
                           CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES usr (id)
);

CREATE TRIGGER UPDATE_ROLE_USR_UPDATE_AT
    BEFORE UPDATE
    ON role_user
    FOR EACH ROW
    EXECUTE PROCEDURE UPDATE_ACCOUNT_UPDATE_AT_COLUMN();

-- DROP TRIGGER IF EXISTS UPDATE_ROLE_ROLE_UPDATE_AT on role_role;
-- DROP TABLE IF EXISTS role_role;
CREATE TABLE role_role (
                           role_id bigint NOT NULL,
                           member_id bigint NOT NULL,
                           created_by_id bigint DEFAULT NULL,
                           created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           last_modified_by_id bigint DEFAULT NULL,
                           updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           version bigint DEFAULT 0,
                           PRIMARY KEY (role_id, member_id),
                           CONSTRAINT role_id FOREIGN KEY (role_id) REFERENCES role (id),
                           CONSTRAINT member_id FOREIGN KEY (member_id) REFERENCES role (id)
);


CREATE TRIGGER UPDATE_ROLE_ROLE_UPDATE_AT
    BEFORE UPDATE
    ON role_role
    FOR EACH ROW
    EXECUTE PROCEDURE UPDATE_ACCOUNT_UPDATE_AT_COLUMN();
