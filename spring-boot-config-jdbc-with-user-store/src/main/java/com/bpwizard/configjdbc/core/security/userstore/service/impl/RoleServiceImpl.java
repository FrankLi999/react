package com.bpwizard.configjdbc.core.security.userstore.service.impl;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.bpwizard.configjdbc.core.jdbc.JdbcUtils;
import com.bpwizard.configjdbc.core.security.userstore.entity.Role;
import com.bpwizard.configjdbc.core.security.userstore.entity.Tenant;
import com.bpwizard.configjdbc.core.security.userstore.entity.User;
import com.bpwizard.configjdbc.core.security.userstore.jdbc.*;
import com.bpwizard.configjdbc.core.security.userstore.service.RoleService;
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
import org.springframework.util.ObjectUtils;


@Service
public class RoleServiceImpl implements RoleService<Role, Long> {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private static final String ROLE_TABLE_PK_COLUMN = "id";
    // private static final String selectAllColumn = "SELECT id, name FROM role WHERE %s";
    // private static final String deleteByIdSql = "DELETE from role where id = :role_id ";
    // private static final String deleteRoleMembershipByIdSql = "DELETE from role_role where role_id = :role_id ";
    // private static final String deleteTenantMembershipByIdSql = "DELETE from tenant_role where role_id = :role_id ";
    // private static final String saveSql = "INSERT INTO role(" +
    //     "created_by_id, name, type" +
    // 	") values(:created_by_id, :name, 'SYSTEM') " +
    //     "ON DUPLICATE KEY UPDATE " +
    //     "last_modified_by_id=:last_modified_by_id, name = :name, version=version+1";

    // private static final String insertSql = "INSERT INTO role(" +
    // 	    "created_by_id, name, type) VALUES(:created_by_id, :name, 'SYSTEM')";

    // private static final String addUserSql = "insert into role_user(created_by_id, user_id, role_id) values(:created_by_id, :user_id, :role_id)";
    // private static final String addRoleSql = "insert into role_role(created_by_id, role_id, member_id) values(:created_by_id, :role_id, :member_id)";
    // private static final String joinTenantSql = "insert into tenant_role(created_by_id, role_id, tenant_id) values(:created_by_id, :role_id, :tenant_id)";
    // private static final String removeUserSql = "DELETE FROM role_user WHERE user_id = :user_id and role_id=:role_id";
    // private static final String removeRoleSql = "DELETE FROM role_role WHERE member_id = :member_id and role_id=:role_id";

    // private static final String findRolesByNameSql = "SELECT mr.id, mr.name FROM role mr " +
    // 		"LEFT JOIN role_role rr ON mr.id = rr.member_id " +
    // 		"LEFT JOIN role r ON r.id = rr.role_id and r.id=:id";
    // private static final String findUsersByNameSql = "SELECT u.id, u.name FROM user u " +
    // 		"LEFT JOIN role_user ur ON u.id = ur.user_id " +
    // 		"LEFT JOIN role r ON r.id = ur.role_id and r.id=:id";

    // private static final String findByIdSql = String.format(selectAllColumn, "id=:id");
    // private static final String findByNameSql = String.format(selectAllColumn, "name=:name");
    // private static final String findAllSql = "SELECT id, name FROM role %s";
    // private static final String findAllNamesSql = "SELECT name FROM role %s";

    // private static final String selectIds = "SELECT id FROM %s WHERE name in (%s)";
    @Autowired
    private AccountSqlStatements accountSqlStatements;

    @Autowired
    @Qualifier("accountDBJdbcTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<Role> findByName(String name) {

        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", name);
        Role role = null;
        try {
            String selectAllColumnCondition = accountSqlStatements.getStatement("roles_findByNameSql_condition");
            String findByNameSql = accountSqlStatements.getStatement("roles_selectAllColumn", selectAllColumnCondition);
            role = jdbcTemplate.queryForObject(
                    findByNameSql, namedParameters, new RoleRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Not able to find role " + name, e);
        }
        return (role == null) ? Optional.empty() : Optional.of(role);
    }

    @Override
    public Role create(Role role) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("created_by_id", WebUtils.currentUserId())
                .addValue("name", role.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertSql = accountSqlStatements.getStatement("roles_insertSql");
        // try {
        jdbcTemplate.update(insertSql, namedParameters, keyHolder, new String[]{ ROLE_TABLE_PK_COLUMN });
        // } catch (DuplicateKeyException ex) {

        // }
        try {
            role.setId(keyHolder.getKeyAs(BigDecimal.class).longValue()); // oracle
        } catch (Exception ex) {
            try {
                role.setId(keyHolder.getKeyAs(Long.class)); // postgres
            } catch (Exception ex1) {
                role.setId(keyHolder.getKeyAs(BigInteger.class).longValue()); // mysql
            }
        }
        if (!ObjectUtils.isEmpty(role.getTenants())) {
            joinTenantsByName(role, role.getTenants());
        }
        return role;
    }
    @Override
    public void save(Role role) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("created_by_id", WebUtils.currentUserId())
                .addValue("name", role.getName())
                .addValue("last_modified_by_id", WebUtils.currentUserId());
        String saveSql = accountSqlStatements.getStatement("roles_saveSql");
        jdbcTemplate.update(saveSql, namedParameters);// params, types);
    }

    @Override
    public Optional<Role> findById(Long id) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
        Role role = null;
        try {
            String findByIdSqlCondition = accountSqlStatements.getStatement("roles_findByIdSql_condition");
            String findByIdSql = accountSqlStatements.getStatement("roles_selectAllColumn", findByIdSqlCondition);
            role = jdbcTemplate.queryForObject(
                    findByIdSql, namedParameters, new RoleRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Not able to find role " + id, e);
        }
        return (role == null) ? Optional.empty() : Optional.of(role);
    }

    @Override
    public boolean existsById(Long id) {
        return this.findById(id).isPresent();
    }

    @Override
    public int deleteById(Long id) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("role_id", id);
        String deleteRoleMembershipByIdSql = accountSqlStatements.getStatement("roles_deleteRoleMembershipByIdSql");
        jdbcTemplate.update(deleteRoleMembershipByIdSql, namedParameters);
        String deleteTenantMembershipByIdSql = accountSqlStatements.getStatement("roles_deleteTenantMembershipByIdSql");
        jdbcTemplate.update(deleteTenantMembershipByIdSql, namedParameters);
        String deleteByIdSql = accountSqlStatements.getStatement("roles_deleteByIdSql");
        return jdbcTemplate.update(deleteByIdSql, namedParameters);

    }

    @Override
    public int delete(Role role) {
        return this.deleteById(role.getId());
    }

    @Override
    public List<String> findAllRoleNames(Pageable pageable) {
        String findAllNamesSql = accountSqlStatements.getStatement("roles_findAllNamesSql");
        return jdbcTemplate.query(
                String.format(findAllNamesSql, JdbcUtils.sqlFragment(pageable)),
                new NameColumnRowMapper());
    }

    @Override
    public List<Role> findAll(Pageable pageable) {
        String findAllSql = accountSqlStatements.getStatement("roles_findAllSql");
        return jdbcTemplate.query(
                String.format(findAllSql, JdbcUtils.sqlFragment(pageable)),
                new RoleRowMapper());
    }

    public int addUser(Role role, User<Long> user) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("created_by_id", WebUtils.currentUserId())
                .addValue("user_id", user.getId())
                .addValue("role_id", role.getId());
        String addUserSql = accountSqlStatements.getStatement("roles_addUserSql");
        return jdbcTemplate.update(addUserSql, namedParameters);
    }

    public int[] addUsers(Role role, List<User<Long>> users) {
        List<SqlParameterSource> batchArgs = new ArrayList<>();
        for (User<Long> user: users) {
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("created_by_id", WebUtils.currentUserId())
                    .addValue("user_id", user.getId())
                    .addValue("role_id", role.getId());
            batchArgs.add(params);
        }
        String addUserSql = accountSqlStatements.getStatement("roles_addUserSql");
        return jdbcTemplate.batchUpdate(addUserSql, batchArgs.toArray(new SqlParameterSource[batchArgs.size()]));
    }

    public int removeUser(Role role, User<Long> user) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", user.getId())
                .addValue("role_id", role.getId());
        String removeUserSql = accountSqlStatements.getStatement("roles_removeUserSql");
        return jdbcTemplate.update(removeUserSql, params);
    }

    public int addRole(Role role, Role member) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("created_by_id", WebUtils.currentUserId())
                .addValue("role_id", role.getId())
                .addValue("member_id", member.getId());
        String addRoleSql = accountSqlStatements.getStatement("roles_addRoleSql");
        return jdbcTemplate.update(addRoleSql, params); //params, types);
    }

    public int[] addRoles(Role role, List<Role> members) {
        List<SqlParameterSource> batchArgs = new ArrayList<>();
        for (Role member: members) {
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("created_by_id", WebUtils.currentUserId())
                    .addValue("role_id", role.getId())
                    .addValue("member_id", member.getId());
            batchArgs.add(params);
        }
        String addRoleSql = accountSqlStatements.getStatement("roles_addRoleSql");
        return jdbcTemplate.batchUpdate(addRoleSql, batchArgs.toArray(new SqlParameterSource[batchArgs.size()]));
    }

    public int[] joinTenants(Role role, List<Tenant> tenants) {
        List<SqlParameterSource> batchArgs = new ArrayList<>();
        for (Tenant tenant: tenants) {
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("created_by_id", WebUtils.currentUserId())
                    .addValue("role_id", role.getId())
                    .addValue("tenant_id", tenant.getId());
            batchArgs.add(params);
        }
        String joinTenantSql = accountSqlStatements.getStatement("roles_joinTenantSql");
        return jdbcTemplate.batchUpdate(joinTenantSql, batchArgs.toArray(new SqlParameterSource[batchArgs.size()]));
    }

    public int[] joinTenantsByName(Role role, Set<String> tenants) {
        List<Long> tenantIds = getIds("tenant", tenants);
        List<SqlParameterSource> batchArgs = new ArrayList<>();
        for (Long tenantId: tenantIds) {
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("created_by_id", WebUtils.currentUserId())
                    .addValue("role_id", role.getId())
                    .addValue("tenant_id", tenantId);
            batchArgs.add(params);
        }
        String joinTenantSql = accountSqlStatements.getStatement("roles_joinTenantSql");
        return jdbcTemplate.batchUpdate(joinTenantSql, batchArgs.toArray(new SqlParameterSource[batchArgs.size()]));
    }

    public int removeRole(Role role, Role member) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("member_id", member.getId())
                .addValue("role_id", role.getId());
        String removeRoleSql = accountSqlStatements.getStatement("roles_removeRoleSql");
        return jdbcTemplate.update(removeRoleSql, params);
    }

    public List<User<Long>> findUsersByName(String role) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", role);
        String findUsersByNameSql = accountSqlStatements.getStatement("roles_findUsersByNameSql");
        return jdbcTemplate.query(
                findUsersByNameSql,
                namedParameters,
                new UserRowMapper());
    }

    public List<Role> findRolesByName(String role) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("name", role);
        String findRolesByNameSql = accountSqlStatements.getStatement("roles_findRolesByNameSql");
        return jdbcTemplate.query(
                findRolesByNameSql,
                namedParameters,
                new RoleRowMapper());
    }

    private String candidateNames(Set<String> names) {
        StringBuilder candidateNames = new StringBuilder();
        int count = 0;
        for (String name: names) {
            if (count > 0) {
                candidateNames.append(", ");
            }
            candidateNames.append("'").append(name).append("'");
            count ++;
        }
        return candidateNames.toString();
    }

    protected List<Long> getIds(String table, Set<String> names) {
        String selectIds = accountSqlStatements.getStatement("roles_selectIds");
        String sql = String.format(selectIds, table, candidateNames(names));
        return jdbcTemplate.query(
                sql,
                new IdColumnRowMapper());
    }
}

