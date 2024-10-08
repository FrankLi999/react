-- DROP TABLE IF EXISTS MY_PROPERTIES;
CREATE TABLE MY_PROPERTIES (
    "PROPERTIES_ID" INT GENERATED BY DEFAULT AS IDENTITY,
    "CREATED_ON" TIMESTAMP (6) DEFAULT CURRENT_TIMESTAMP NOT NULL ENABLE,
    APPLICATION VARCHAR(255),
    PROFILE VARCHAR(255),
    LABEL VARCHAR(255),
    PROP_KEY VARCHAR(1023),
    PROP_VALUE VARCHAR(1023),
    PRIMARY KEY ("PROPERTIES_ID")
);


CREATE INDEX DATA_INDEX1
    ON MY_PROPERTIES(APPLICATION, PROFILE, LABEL);


-- CCPay ODR Config
INSERT INTO MY_PROPERTIES (
    APPLICATION,
    PROFILE,
    LABEL,
    PROP_KEY,
    PROP_VALUE
) VALUES (
    'my-app',
    'default',
    'master',
    'my.request-url',
    'https://my-default/v1/service'
);


INSERT INTO MY_PROPERTIES (
    APPLICATION,
    PROFILE,
    LABEL,
    PROP_KEY,
    PROP_VALUE
) VALUES (
    'my-app',
    'dev',
    'master',
    'my.request-url',
    'https://my-dev/v1/service'
);

