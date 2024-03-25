package com.bpwizard.configjdbc.core.jdbc;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.EnumSet;
import java.util.Objects;

import com.bpwizard.configjdbc.core.security.userstore.entity.SpringEntity;
import com.bpwizard.configjdbc.core.exception.RelationalProviderException;
import com.bpwizard.configjdbc.core.exception.VersionException;
import com.bpwizard.configjdbc.core.web.EnviornmentUtils;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import liquibase.integration.spring.SpringLiquibase;

@SuppressWarnings("deprecation")
public class JdbcUtils {
    private static final EnumSet<DatabaseType.Name> SUPPORTED_DBS = EnumSet.of(DatabaseType.Name.H2,
            DatabaseType.Name.MYSQL,
            DatabaseType.Name.ORACLE,
            //DatabaseType.Name.SQLSERVER,
            DatabaseType.Name.POSTGRES);
    /**
     * A convenient method for running code
     * after successful database commit.
     *
     * @param runnable
     */
    public static void afterCommit(Runnable runnable) {

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {

                        runnable.run();
                    }
                });
    }


    /**
     * Throws a VersionException if the versions of the
     * given entities aren't same.
     *
     * @param original
     * @param updated
     */
    public static <ID extends Serializable>
    void ensureCorrectVersion(SpringEntity<ID> original, SpringEntity<ID> updated) {

        if (original.getVersion() != updated.getVersion())
            throw new VersionException(original.getClass().getSimpleName(), original.getId().toString());
    }

    public static String sqlFragment(Pageable pageable) {
        StringBuilder sqlFragment = new StringBuilder("");
        if (pageable.getSort().isSorted()) {
            sqlFragment.append("ORDER BY ");
            int count = 0;
            for (Order order: pageable.getSort().toList()) {
                if (count > 0) {
                    sqlFragment.append(", ");
                }
                sqlFragment.append(order.getProperty()).append(" ").append(order.getDirection().name());
                count++;
            }
        }
        if (pageable.isPaged()) {
            sqlFragment.append(" LIMIT").append(pageable.getPageSize()).append(" OFFSET").append(pageable.getOffset());
        }
        return sqlFragment.toString();
    }

    public static DataSource getDataSource(String propertyPrefix, Environment env) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(env.getProperty(String.format("%s.%s", propertyPrefix, "url")));
        config.setDriverClassName(env.getProperty(String.format("%s.%s", propertyPrefix, "driver-class-name")));
        config.setUsername(env.getProperty(String.format("%s.%s", propertyPrefix, "username")));
        config.setPassword(env.getProperty(String.format("%s.%s", propertyPrefix, "password")));

        config.setConnectionTestQuery(env.getProperty(String.format("%s.hikari.%s", propertyPrefix, "connection-test-query")));
        config.setIdleTimeout(EnviornmentUtils.getLong(env, String.format("%s.hikari.%s", propertyPrefix, "idle-timeout"), 600000));
        config.setMaxLifetime(EnviornmentUtils.getLong(env, String.format("%s.hikari.%s", propertyPrefix, "max-lifetime"), 1800000));
        config.setMinimumIdle(EnviornmentUtils.getInt(env, String.format("%s.hikari.%s", propertyPrefix, "minimum-idle"), 5));
        config.setMaximumPoolSize(EnviornmentUtils.getInt(env, String.format("%s.hikari.%s", propertyPrefix, "maximum-pool-size"), 5));
        config.setConnectionTimeout(EnviornmentUtils.getLong(env, String.format("%s.hikari.%s", propertyPrefix, "connection-timeout"), 30000));

        HikariDataSource hikariDS = new HikariDataSource(config);
        return hikariDS;
    }

    /**
     * Determine the type of a database, based on the metadata information from the DB metadata.
     *
     * @param metaData a {@link DatabaseMetaData} instance, may not be null
     * @return a {@link DatabaseType} instance, never null
     * @throws SQLException if a database access error occurs
     * or this method is called on a closed connection
     */
    public static DatabaseType determineType(DataSource dataSource) {
        // DatabaseMetaData metaData
        try (Connection connection = newConnection(dataSource, false, true)) {
            DatabaseMetaData metaData = connection.getMetaData();
            DatabaseType dbType = DatabaseType.UNKNOWN;

            metaData = Objects.requireNonNull(metaData, "metaData cannot be null");
            int majorVersion = metaData.getDatabaseMajorVersion();
            int minorVersion = metaData.getDatabaseMinorVersion();
            String name = metaData.getDatabaseProductName().toLowerCase();
            if (name.contains("mysql")) {
                dbType = new DatabaseType(DatabaseType.Name.MYSQL, majorVersion, minorVersion);
            } else if (name.contains("postgres")) {
                dbType = new DatabaseType(DatabaseType.Name.POSTGRES, majorVersion, minorVersion);
            } else if (name.contains("derby")) {
                dbType = new DatabaseType(DatabaseType.Name.DERBY, majorVersion, minorVersion);
            } else if (name.contains("hsql") || name.toLowerCase().contains("hypersonic")) {
                dbType = new DatabaseType(DatabaseType.Name.HSQL, majorVersion, minorVersion);
            } else if (name.contains("h2")) {
                dbType = new DatabaseType(DatabaseType.Name.H2, majorVersion, minorVersion);
            } else if (name.contains("sqlite")) {
                dbType = new DatabaseType(DatabaseType.Name.SQLITE, majorVersion, minorVersion);
            } else if (name.contains("db2")) {
                dbType = new DatabaseType(DatabaseType.Name.DB2, majorVersion, minorVersion);
            } else if (name.contains("informix")) {
                dbType = new DatabaseType(DatabaseType.Name.INFORMIX, majorVersion, minorVersion);
            } else if (name.contains("interbase")) {
                dbType = new DatabaseType(DatabaseType.Name.INTERBASE, majorVersion, minorVersion);
            } else if (name.contains("firebird")) {
                dbType = new DatabaseType(DatabaseType.Name.FIREBIRD, majorVersion, minorVersion);
            } else if (name.contains("sqlserver") || name.toLowerCase().contains("microsoft")) {
                dbType = new DatabaseType(DatabaseType.Name.SQLSERVER, majorVersion, minorVersion);
            } else if (name.contains("access")) {
                dbType = new DatabaseType(DatabaseType.Name.ACCESS, majorVersion, minorVersion);
            } else if (name.contains("oracle")) {
                dbType = new DatabaseType(DatabaseType.Name.ORACLE, majorVersion, minorVersion);
            } else if (name.contains("adaptive")) {
                dbType = new DatabaseType(DatabaseType.Name.SYBASE, majorVersion, minorVersion);
            } else if (name.contains("cassandra")) {
                dbType = new DatabaseType(DatabaseType.Name.CASSANDRA, majorVersion, minorVersion);
            }
            if (!SUPPORTED_DBS.contains(dbType.name())) {
                throw new RelationalProviderException(String.format("Unsupported database type %s", dbType.name()));
            }
            return dbType;
        } catch (SQLException e) {
            throw new RelationalProviderException(e);
        }
    }

    protected static Connection newConnection(DataSource dataSource, boolean autocommit, boolean readonly) {
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(autocommit);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setReadOnly(readonly);
            return connection;
        } catch (SQLException e) {
            throw new RelationalProviderException(e);
        }
    }

    public static SpringLiquibase springLiquibase(DataSource dataSource, LiquibaseProperties properties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setContexts(properties.getContexts());
        liquibase.setDefaultSchema(properties.getDefaultSchema());
        liquibase.setDropFirst(properties.isDropFirst());
        liquibase.setShouldRun(properties.isEnabled());
        // liquibase.setLabels(properties.getLabels());
        liquibase.setChangeLogParameters(properties.getParameters());
        liquibase.setRollbackFile(properties.getRollbackFile());
        liquibase.setChangeLogParameters(properties.getParameters());
        return liquibase;
    }
}
