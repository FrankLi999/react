package com.bpwizard.configjdbc.core.security.userstore.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.bpwizard.configjdbc.core.security.AuthProvider;
import com.bpwizard.configjdbc.core.security.userstore.entity.User;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User<Long>> {
    @Override
    public User<Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
        User<Long> user = new User<Long>();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setImageUrl(rs.getString("image_url"));
        user.setProvider(AuthProvider.valueOf(rs.getString("provider")));
        user.setProviderId(rs.getString("provider_id"));
        user.setVersion(rs.getLong("version"));
        user.setCredentialsUpdatedMillis(rs.getLong("credentials_updated_millis"));
        return user;
    }
}
