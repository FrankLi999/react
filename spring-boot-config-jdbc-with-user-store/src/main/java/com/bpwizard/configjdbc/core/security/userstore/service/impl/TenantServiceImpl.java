package com.bpwizard.configjdbc.core.security.userstore.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bpwizard.configjdbc.core.jdbc.JdbcUtils;
import com.bpwizard.configjdbc.core.security.userstore.entity.Role;
import com.bpwizard.configjdbc.core.security.userstore.entity.Tenant;
import com.bpwizard.configjdbc.core.security.userstore.entity.User;
import com.bpwizard.configjdbc.core.security.userstore.jdbc.*;
import com.bpwizard.configjdbc.core.security.userstore.service.TenantService;
import com.bpwizard.configjdbc.core.web.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

@Service
public class TenantServiceImpl implements TenantService<Tenant, Long> {
    private static final Logger logger = LoggerFactory.getLogger(TenantServiceImpl.class);
    private static final String TENANTE_TABLE_PK_COLUMN = "id";
    // private static final String selectAllColumn = "SELECT id, name FROM tenant WHERE %s";
    // private static final String deleteByIdSql = "DELETE from tenant where id = :tenant_id ";
    // private static final String deleteRoleMembershipByIdSql = "DELETE from tenant_role where tenant_id = :tenant_id ";
    // private static final String deleteUserMembershipByIdSql = "DELETE from tenant_user where tenant_id = :tenant_id ";
    // private static final String saveSql = "INSERT tenant(" +
    //     "created_by_id, name" +
    // 	") values(:created_by_id, :name) " +
    //     "ON DUPLICATE KEY UPDATE " +
    //     "last_modified_by_id= :last_modified_by_id, name = :name, version=version+1";
    // private static final String insertSql = "INSERT tenant(" +
    // 	    "created_by_id, name) values(:created_by_id, :name)";

    // private static final String addUserSql = "insert into tenant_user(created_by_id, user_id, tenant_id) values(:created_by_id, :user_id, :tenant_id)";
    // private static final String addRoleSql = "insert into tenant_role(created_by_id, role_id, tenant_id) values(:created_by_id, :role_id, :tenant_id)";

    // private static final String removeUserSql = "DELETE FROM tenant_user WHERE user_id = :user_id and tenant_id= :tenant_id";
    // private static final String removeRoleSql = "DELETE FROM tenant_role WHERE role_id = :role_id and tenant_id= :tenant_id";

    // private static final String findRolesByNameSql = "SELECT r.id, r.name FROM role r " +
    // 		"LEFT JOIN tenant_role tr ON r.id = tr.role_id " +
    // 		"LEFT JOIN tenant t ON t.id = tr.tenant_id and t.id=:id";
    // private static final String findUsersByNameSql = "SELECT u.id, u.name FROM usr u " +
    // 		"LEFT JOIN tenant_user tu ON u.id = tu.user_id " +
    // 		"LEFT JOIN tenant t ON t.id = tu.tenant_id and t.id=:id";

    // private static final String findByIdSql = String.format(selectAllColumn, "id=:id");
    // private static final String findByNameSql = String.format(selectAllColumn, "name=:name");
    // private static final String findAllSql = "SELECT id, name FROM tenant %s";
    // private static final String findAllNamesSql = "SELECT name FROM tenant %s";
    @Autowired
    private AccountSqlStatements accountSqlStatements;
    @Autowired
    @Qualifier("accountDBJdbcTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;


    @Override
    public Optional<Tenant> findByName(String name) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", name);
        Tenant tenant = null;
        try {
            String findByNameSqlCondition = accountSqlStatements.getStatement("tenants_findByNameSql_condition");
            String findByNameSql = accountSqlStatements.getStatement("tenants_selectAllColumn", findByNameSqlCondition);
            tenant = jdbcTemplate.queryForObject(
                    findByNameSql, namedParameters, new TenantRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Not able to find tenant " + name, e);
        }
        return (tenant == null) ? Optional.empty() : Optional.of(tenant);
    }

    @Override
    public Tenant create(Tenant tenant) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("created_by_id", WebUtils.currentUserId())
                .addValue("name", tenant.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertSql = accountSqlStatements.getStatement("tenants_insertSql", new String[] {TENANTE_TABLE_PK_COLUMN});
        jdbcTemplate.update(insertSql, namedParameters, keyHolder);
        try {
            tenant.setId(keyHolder.getKeyAs(BigDecimal.class).longValue()); // oracle
        } catch (Exception ex) {
            try {
                tenant.setId(keyHolder.getKeyAs(Long.class)); // postgres
            } catch (Exception ex1) {
                tenant.setId(keyHolder.getKeyAs(BigInteger.class).longValue()); // mysql
            }
        }
        return tenant;
    }

    @Override
    public void save(Tenant tenant) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("created_by_id", WebUtils.currentUserId())
                .addValue("last_modified_by_id", WebUtils.currentUserId())
                .addValue("name", tenant.getName());
        String saveSql = accountSqlStatements.getStatement("tenants_saveSql");
        jdbcTemplate.update(saveSql, namedParameters); // params, types);
    }

    @Override
    public Optional<Tenant> findById(Long id) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
        Tenant tenant = null;
        try {
            String findByIdSqlCondition = accountSqlStatements.getStatement("tenants_findByIdSql_condition");
            String findByIdSql = accountSqlStatements.getStatement("tenants_selectAllColumn", findByIdSqlCondition);
            tenant = jdbcTemplate.queryForObject(
                    findByIdSql, namedParameters, new TenantRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Not able to find tenant " + id, e);
        }
        return (tenant == null) ? Optional.empty() : Optional.of(tenant);
    }

    @Override
    public boolean existsById(Long id) {
        return this.findById(id).isPresent();
    }

    @Override
    public int deleteById(Long id) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("tenant_id", id);
        String deleteRoleMembershipByIdSql = accountSqlStatements.getStatement("tenants_deleteRoleMembershipByIdSql");
        jdbcTemplate.update(deleteRoleMembershipByIdSql, namedParameters); //params, types);
        String deleteUserMembershipByIdSql = accountSqlStatements.getStatement("tenants_deleteUserMembershipByIdSql");
        jdbcTemplate.update(deleteUserMembershipByIdSql, namedParameters); //params, types);
        String deleteByIdSql = accountSqlStatements.getStatement("tenants_deleteByIdSql");
        return jdbcTemplate.update(deleteByIdSql, namedParameters); //params, types);
    }

    @Override
    public int delete(Tenant tenant) {
        return this.deleteById(tenant.getId());
    }

    @Override
    public List<Tenant> findAll(Pageable pageable) {
        String findAllSql = accountSqlStatements.getStatement("tenants_findAllSql");
        return jdbcTemplate.query(
                String.format(findAllSql, JdbcUtils.sqlFragment(pageable)),
                new TenantRowMapper());
    }

    @Override
    public List<String> findAllTenantNames(Pageable pageable) {
        String findAllNamesSql = accountSqlStatements.getStatement("tenants_findAllNamesSql");
        return jdbcTemplate.query(
                String.format(findAllNamesSql, JdbcUtils.sqlFragment(pageable)),
                new NameColumnRowMapper());
    }

    @Override
    public int addUser(Tenant tenant, User<Long> user) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("created_by_id", WebUtils.currentUserId())
                .addValue("user_id", user.getId())
                .addValue("tenant_id", tenant.getId());
        String addUserSql = accountSqlStatements.getStatement("tenants_addUserSql");
        return jdbcTemplate.update(addUserSql, params); //params, types);
    }

    @Override
    public int[] addUsers(Tenant tenant, List<User<Long>> users) {
        List<SqlParameterSource> batchArgs = new ArrayList<>();
        for (User<Long> user: users) {
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("created_by_id", WebUtils.currentUserId())
                    .addValue("user_id", user.getId())
                    .addValue("tenant_id", tenant.getId());
            batchArgs.add(params);
        }

        // int[] types = {Types.BIGINT, Types.BIGINT, Types.BIGINT};
        String addUserSql = accountSqlStatements.getStatement("tenants_addUserSql");
        return jdbcTemplate.batchUpdate(addUserSql, batchArgs.toArray(new SqlParameterSource[batchArgs.size()])); //batchArgs, types);
    }

    @Override
    public int addRole(Tenant tenant, Role role) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("created_by_id", WebUtils.currentUserId())
                .addValue("role_id", role.getId())
                .addValue("tenant_id", tenant.getName());
        String addRoleSql = accountSqlStatements.getStatement("tenants_addRoleSql");
        return jdbcTemplate.update(addRoleSql, params);
    }

    @Override
    public int[] addRoles(Tenant tenant, List<Role> roles) {
        List<SqlParameterSource> batchArgs = new ArrayList<>();
        for (Role role: roles) {
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("created_by_id", WebUtils.currentUserId())
                    .addValue("role_id", role.getId())
                    .addValue("tenant_id", tenant.getName());
            batchArgs.add(params);
        }
        String addRoleSql = accountSqlStatements.getStatement("tenants_addRoleSql");
        return jdbcTemplate.batchUpdate(addRoleSql, batchArgs.toArray(new SqlParameterSource[batchArgs.size()]));
    }

    @Override
    public int removeUser(Tenant tenant, User<Long> user) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", user.getId())
                .addValue("tenant_id", tenant.getName());
        String removeUserSql = accountSqlStatements.getStatement("tenants_removeUserSql");
        return jdbcTemplate.update(removeUserSql, params);
    }

    @Override
    public int removeRole(Tenant tenant, Role role) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("role_id", role.getId())
                .addValue("tenant_id", tenant.getName());
        String removeRoleSql = accountSqlStatements.getStatement("tenants_removeRoleSql");
        return jdbcTemplate.update(removeRoleSql, params);
    }

    @Override
    public List<User<Long>> findUsersByName(String tenant) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", tenant);
        String findUsersByNameSql = accountSqlStatements.getStatement("tenants_findUsersByNameSql");
        return jdbcTemplate.query(
                findUsersByNameSql,
                namedParameters,
                new UserRowMapper());
    }

    @Override
    public List<Role> findRolesByName(String tenant) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", tenant);
        String findRolesByNameSql = accountSqlStatements.getStatement("tenants_findRolesByNameSql");
        return jdbcTemplate.query(
                findRolesByNameSql,
                namedParameters,
                new RoleRowMapper());
    }
}

