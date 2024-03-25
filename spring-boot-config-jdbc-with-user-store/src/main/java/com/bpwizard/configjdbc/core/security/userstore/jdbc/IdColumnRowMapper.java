package com.bpwizard.configjdbc.core.security.userstore.jdbc;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class IdColumnRowMapper implements RowMapper<Long> {
    @Override
    public  Long mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("id");
    }
}