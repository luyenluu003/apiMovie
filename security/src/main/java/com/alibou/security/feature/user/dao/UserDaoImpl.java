package com.alibou.security.feature.user.dao;

import com.alibou.security.feature.notification.model.Notification;
import com.alibou.security.feature.user.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@Log4j2
public class UserDaoImpl implements UserDao{

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<User> findAllUser() {
        try {
            String sql = "SELECT * FROM users";

            return jdbcTemplate.query(sql, (rs, rowNum) -> User.builder()
                    .userId(rs.getString("id"))
                    .userName(rs.getString("username"))
                    .email(rs.getString("email"))
                    .build());
        } catch (Exception e) {
            log.error("Error while getting user list", e);
        }
        return Collections.emptyList();
    }

}
