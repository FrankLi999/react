package com.bpwizard.configjdbc.core.security.userstore.jdbc;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bpwizard.configjdbc.core.security.userstore.entity.Tenant;
import org.springframework.jdbc.core.RowMapper;

public class TenantRowMapper  implements RowMapper<Tenant> {
    @Override
    public Tenant mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tenant Tenant = new Tenant();
        Tenant.setId(rs.getLong("id"));
        Tenant.setName(rs.getString("name"));
        return Tenant;
    }
}