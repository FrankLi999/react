package com.bpwizard.configjdbc.core.security.userstore.jdbc;


import java.io.IOException;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import com.bpwizard.configjdbc.core.exception.RelationalProviderException;
import com.bpwizard.configjdbc.core.jdbc.DatabaseType;
import com.bpwizard.configjdbc.core.jdbc.JdbcUtils;
import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AccountSqlStatements {
    Map<String, String> statements;
    private ResourceLoader resourceLoader;
    private DataSource accountDBDatasource;
    @Autowired
    public AccountSqlStatements(@Qualifier("accountDBDatasource") DataSource accountDBDatasource, ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.accountDBDatasource = accountDBDatasource;
    }

    @PostConstruct
    public void loadSqlStatements() {
        try {
            DatabaseType dbType = JdbcUtils.determineType(accountDBDatasource);
            Properties sqlProps = new Properties();
            sqlProps.load(this.resourceLoader.getResource(String.format("classpath:db/%s/account_sql.properties", dbType.nameString().toLowerCase())).getInputStream());
            statements = sqlProps.entrySet().stream().collect(
                    Collectors.toMap(
                            e -> String.valueOf(e.getKey()),
                            e -> String.valueOf(e.getValue()),
                            (prev, next) -> next, HashMap::new
                    ));
        } catch (IOException e) {
            throw new RelationalProviderException(e);
        }
    }

    public String getStatement(String statementKey) {
        String statement = statements.get(statementKey);
        if (!StringUtils.hasText(statement))    {
            throw new RelationalProviderException(String.format("Statement not provided for statement key %s", statementKey));
        }
        return statement;
    }

    public String getStatement(String statementKey, Object... args) {
        String statement = statements.get(statementKey);
        if (!StringUtils.hasText(statement))    {
            throw new RelationalProviderException(String.format("Statement not provided for statement key %s", statementKey));
        }
        return String.format(statement, args);
    }
}
