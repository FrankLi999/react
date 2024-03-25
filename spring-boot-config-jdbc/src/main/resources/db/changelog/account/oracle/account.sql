-- https://docs.oracle.com/cd/E12151_01/doc.150/e12155/oracle_mysql_compared.htm
-- DROP TABLE IF EXISTS usr;
CREATE TABLE usr (
                     ID NUMBER(19, 0) GENERATED ALWAYS AS IDENTITY,
                     created_by_id NUMBER(19, 0) DEFAULT NULL,
                     created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL ,
                     last_modified_by_id NUMBER(19, 0) DEFAULT NULL,
                     updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                     version NUMBER(19, 0) DEFAULT 0,
                     attempts Number(10, 0) DEFAULT 0 NOT NULL ,
                     credentials_updated_millis NUMBER(19, 0) DEFAULT 0 NOT NULL ,
                     email NVARCHAR2(250) NOT NULL UNIQUE,
                     email_verified NUMBER(1, 0) DEFAULT NULL,
                     first_name NVARCHAR2(50) DEFAULT NULL,
                     image_url NVARCHAR2(255) DEFAULT NULL,
                     last_name NVARCHAR2(50) DEFAULT NULL,
                     lock_expiration_time TIMESTAMP WITH TIME ZONE DEFAULT NULL,
                     name NVARCHAR2(50) NOT NULL UNIQUE,
                     new_email NVARCHAR2(250) DEFAULT NULL,
                     new_password NVARCHAR2(255) DEFAULT NULL,
                     password NVARCHAR2(255) NOT NULL,
                     provider NVARCHAR2(255) DEFAULT 'local',
                     provider_id NVARCHAR2(255) DEFAULT NULL,
                     salt NVARCHAR2(255) DEFAULT NULL,
                     PRIMARY KEY (id)
)
//
create trigger set_usr_updated_at
  before update on usr
  for each row
begin
  :new.updated_at := current_timestamp();
end
//
-- DROP TRIGGER IF EXISTS UPDATE_ROLE_UPDATE_AT ON role;
-- DROP TABLE IF EXISTS role;
CREATE TABLE role (
  ID NUMBER(19, 0) GENERATED ALWAYS AS IDENTITY,
  created_by_id NUMBER(19, 0) DEFAULT NULL,
  created_a TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL ,
  last_modified_by_id NUMBER(19, 0) DEFAULT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  version NUMBER(19, 0) DEFAULT 0,
  name NVARCHAR2(50) NOT NULL UNIQUE,
  type NVARCHAR2(50) NOT NULL,
  PRIMARY KEY (id)
)
//
create trigger set_role_updated_at
  before update on role
  for each row
begin
  :new.updated_at := current_timestamp();
end
//
-- DROP TRIGGER IF EXISTS UPDATE_TENANT_UPDATE_AT on tenant;
-- DROP TABLE IF EXISTS tenant;
CREATE TABLE tenant (
  ID NUMBER(19, 0) GENERATED ALWAYS AS IDENTITY,
  created_by_id NUMBER(19, 0) DEFAULT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL ,
  last_modified_by_id NUMBER(19, 0) DEFAULT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  version NUMBER(19, 0) DEFAULT 0,
  name NVARCHAR2(50) NOT NULL UNIQUE,
  PRIMARY KEY (id)
)
//
create trigger set_tenant_updated_at
  before update on role
  for each row
begin
  :new.updated_at := current_timestamp();
end
//
-- DROP TRIGGER IF EXISTS UPDATE_TENANT_ROLE_UPDATE_AT on tenant_role;
-- DROP TABLE IF EXISTS tenant_role;
CREATE TABLE tenant_role (
  tenant_id NUMBER(19, 0) NOT NULL,
  role_id NUMBER(19, 0) NOT NULL,
  created_by_id NUMBER(19, 0) DEFAULT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  last_modified_by_id NUMBER(19, 0) DEFAULT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  version NUMBER(19, 0) DEFAULT 0,  
  PRIMARY KEY (role_id, tenant_id),
  CONSTRAINT tenant_role_tenant_id FOREIGN KEY (tenant_id) REFERENCES tenant (id),
  CONSTRAINT tenant_role_role_id FOREIGN KEY (role_id) REFERENCES role (id)
)
//
create trigger set_tenant_role_updated_at
  before update on tenant_role
  for each row
begin
  :new.updated_at := current_timestamp();
end
//
-- DROP TRIGGER IF EXISTS UPDATE_TENANT_USR_UPDATE_AT on tenant_user;
-- DROP TABLE IF EXISTS tenant_user;
CREATE TABLE tenant_user (
  user_id NUMBER(19, 0) NOT NULL,
  tenant_id NUMBER(19, 0) NOT NULL,
  created_by_id NUMBER(19, 0) DEFAULT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  last_modified_by_id NUMBER(19, 0) DEFAULT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  version NUMBER(19, 0) DEFAULT 0,
  PRIMARY KEY (user_id, tenant_id),
  CONSTRAINT tenant_user_tenant_id FOREIGN KEY (tenant_id) REFERENCES tenant (id),
  CONSTRAINT tenant_user_usr_id FOREIGN KEY (user_id) REFERENCES usr(id)
)
//
create trigger set_tenant_user_updated_at
  before update on tenant_user
  for each row
begin
  :new.updated_at := current_timestamp();
end
//
-- DROP TRIGGER IF EXISTS UPDATE_ROLE_USR_UPDATE_AT on role_user;
-- DROP TABLE IF EXISTS role_user;
CREATE TABLE role_user (
  user_id NUMBER(19, 0) NOT NULL,
  role_id NUMBER(19, 0) NOT NULL,
  created_by_id NUMBER(19, 0) DEFAULT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  last_modified_by_id NUMBER(19, 0) DEFAULT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  version NUMBER(19, 0) DEFAULT 0,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT role_user_role_id FOREIGN KEY (role_id) REFERENCES role (id),
  CONSTRAINT role_user_user_id FOREIGN KEY (user_id) REFERENCES usr (id)
)
//
create trigger set_role_user_updated_at
  before update on role_user
  for each row
begin
  :new.updated_at := current_timestamp();
end
//
-- DROP TRIGGER IF EXISTS UPDATE_ROLE_ROLE_UPDATE_AT on role_role;
-- DROP TABLE IF EXISTS role_role;
CREATE TABLE role_role (
  role_id NUMBER(19, 0) NOT NULL,
  member_id NUMBER(19, 0) NOT NULL,
  created_by_id NUMBER(19, 0) DEFAULT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  last_modified_by_id NUMBER(19, 0) DEFAULT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  version NUMBER(19, 0) DEFAULT 0,
  PRIMARY KEY (role_id, member_id),
  CONSTRAINT role_role_role_id FOREIGN KEY (role_id) REFERENCES role (id),
  CONSTRAINT role_role_member_id FOREIGN KEY (member_id) REFERENCES role (id)
)
//
create trigger set_role_role_updated_at
  before update on role_role
  for each row
begin
  :new.updated_at := current_timestamp();
end
//