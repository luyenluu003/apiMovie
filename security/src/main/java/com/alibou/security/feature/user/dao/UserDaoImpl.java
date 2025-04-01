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

            log.info("TIM THANH CONG USER =>", userId);

            return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) ->
                    User.builder()
                            .userId(rs.getString("user_id")).userName(rs.getString("username"))
                            .avatar(rs.getString("avatar"))
                            .email(rs.getString("email"))
                            .phoneNumber(rs.getString("phone_number"))
                            .vipEndDate(rs.getDate("vip_end_date"))
                            .vipStartDate(rs.getDate("vip_start_date"))
                            .vipLevel(rs.getString("vip_level"))
                            .build()
            );

        } catch (EmptyResultDataAccessException e) {
            log.warn("User not found with userId: {}", userId);
        } catch (Exception e) {
            log.error("Error retrieving User: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Integer updateUserVip(User user) {
        String sql = "UPDATE users SET  vip_start_date = :vipStartDate, vip_end_date = :vipEndDate, vip_level =:vipLevel WHERE user_id = :userId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", user.getUserId())
                .addValue("vipStartDate", user.getVipStartDate())
                .addValue("vipEndDate", user.getVipEndDate())
                .addValue("vipLevel", user.getVipLevel());
        try {
            return jdbcTemplate.update(sql, params);
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật thông tin người dùng vip với userId: {}", user.getUserId(), e);
            return 0;
        }
    }


}
