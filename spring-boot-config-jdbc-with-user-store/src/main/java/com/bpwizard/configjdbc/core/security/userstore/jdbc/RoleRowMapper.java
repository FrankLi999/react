package com.bpwizard.configjdbc.core.security.userstore.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.bpwizard.configjdbc.core.security.userstore.entity.Role;
import org.springframework.jdbc.core.RowMapper;

public class RoleRowMapper implements RowMapper<Role> {
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role = new Role();
        role.setId(rs.getLong("id"));
        role.setName(rs.getString("name"));
        return role;
    }
}