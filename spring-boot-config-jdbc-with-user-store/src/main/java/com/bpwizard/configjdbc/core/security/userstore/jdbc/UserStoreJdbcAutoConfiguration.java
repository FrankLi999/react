package com.bpwizard.configjdbc.core.security.userstore.jdbc;
import javax.sql.DataSource;

import com.bpwizard.configjdbc.core.jdbc.JdbcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;


import liquibase.integration.spring.SpringLiquibase;


@Configuration
@EnableTransactionManagement
//@EnableJdbcAuditing
@EnableJdbcRepositories
// @AutoConfigureBefore({CommonsWebAutoConfiguration.class})
public class UserStoreJdbcAutoConfiguration {
    @Autowired
    private Environment env;

    @Bean(name = "accountDBJdbcTemplate")
    @ConditionalOnMissingBean(name = "accountDBJdbcTemplate")
    public NamedParameterJdbcTemplate accountDBJdbcTemplate(@Qualifier("accountDBDatasource") DataSource accountDBDatasource) {
        return new NamedParameterJdbcTemplate(accountDBDatasource);
    }

    @Bean(name="accountDBDatasource")
    @ConditionalOnMissingBean(name="accountDBDatasource")
    public DataSource accountDBDatasource() {
        return JdbcUtils.getDataSource("bpw.account.datasource", env);
    }

    @Bean(name="accountLiquibaseProperties")
    @ConfigurationProperties(prefix = "bpw.account.liquibase")
    public LiquibaseProperties accountLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean(name="accountSpringLiquibase")
    public SpringLiquibase accountLiquibase(@Qualifier("accountDBDatasource") DataSource accountDBDatasource, @Qualifier("accountLiquibaseProperties") LiquibaseProperties accountLiquibaseProperties) {
        return JdbcUtils.springLiquibase(accountDBDatasource, accountLiquibaseProperties);
    }
}

