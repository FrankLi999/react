# create user

create user C##Camel identified by Passw0rd;
grant sysdba to C##Camel container=all

GRANT CREATE SESSION TO C##Camel;


GRANT RESOURCE TO C##Camel;

ALTER USER C##Camel quota unlimited on USERS;
##############

DROP TABLE IF EXISTS MY_PROPERTIES;

CREATE TABLE APP_PROPERTIES (
"PROPERTIES_ID" INT GENERATED BY DEFAULT AS IDENTITY,
"CREATED_ON" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP NOT NULL ENABLE,
APPLICATION VARCHAR(255),
PROFILE VARCHAR(255),
LABEL VARCHAR(255),
PROP_KEY VARCHAR(1023),
PROP_VALUE VARCHAR(1023),
PRIMARY KEY ("PROPERTIES_ID")
);

CREATE INDEX DATA_INDEX
ON MY_PROPERTIES(APPLICATION, PROFILE, LABEL);

###########

## Account expired
SELECT username, account_status FROM dba_users;
SELECT username, account_status, expiry_date FROM dba_users
ALTER PROFILE DEFAULT LIMIT PASSWORD_LIFE_TIME UNLIMITED;
ALTER PROFILE DEFAULT LIMIT FAILED_LOGIN_ATTEMPTS UNLIMITED PASSWORD_LIFE_TIME UNLIMITED;

unlock:
ALTER USER C##CAMEL ACCOUNT UNLOCK;

C##CAMEL

C##ACCOUNT

C##WCM_MINIMAL

C##WCM_AUTHORING

C##BPM

C##BPM_SITE

C##WCMM_SITE


expire it:
 ALTER USER C##CAMEL PASSWORD EXPIRE;

activate expired user:
 Alter user C##CAMEL IDENTIFIED BY "Passw0rd";  
 Alter user C##ACCOUNT IDENTIFIED BY "Passw0rd"; 
 Alter user C##WCM_MINIMAL IDENTIFIED BY "Passw0rd"; 
 Alter user C##BPM IDENTIFIED BY "Passw0rd"; 