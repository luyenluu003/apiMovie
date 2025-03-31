package com.alibou.security.feature.user.dao;

import com.alibou.security.feature.movie.model.Movie;
import com.alibou.security.feature.notification.model.Notification;
import com.alibou.security.feature.user.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public User userFindByuserId(String userId){
        try {
            String sql = "SELECT * FROM users " +
                    "WHERE user_id = :userId and active = 1 ";

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("userId", userId);

            return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) ->
                    User.builder()
                            .userId(rs.getString("user_id")).userName(rs.getString("username"))
                            .avatar(rs.getString("avatar"))
                            .email(rs.getString("email"))
                            .phoneNumber(rs.getString("phone_number"))
                            .build()
            );

        } catch (EmptyResultDataAccessException e) {
            log.warn("User not found with userId: {}", userId);
        } catch (Exception e) {
            log.error("Error retrieving User: {}", e.getMessage(), e);
        }
        return null;
    }

}
