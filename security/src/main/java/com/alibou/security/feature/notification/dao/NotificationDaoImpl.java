package com.alibou.security.feature.notification.dao;

import com.alibou.security.feature.episode.model.Episode;
import com.alibou.security.feature.movie.model.Movie;
import com.alibou.security.feature.notification.model.Notification;
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
public class NotificationDaoImpl implements NotificationDao{
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<Notification> findByStatus(Integer status) {
        try{
            String sql = String.format(
                    "SELECT id, user_id, message, title, image_url, related_id, start_at, end_at " +
                            "FROM movie_notification " +
                            "WHERE status =:status " +
                            "AND DATE(start_at) <= DATE(NOW()) " +
                            "AND DATE(end_at) >= DATE(NOW()) " +
                            "ORDER BY start_at DESC "
            );

            SqlParameterSource params = new MapSqlParameterSource("status", status);

            return jdbcTemplate.query(sql, params, (rs, rowNum) -> Notification.builder()
                    .id(rs.getLong("id"))
                    .userId(rs.getString("user_id"))
                    .message(rs.getString("message"))
                    .title(rs.getString("title"))
                    .imageUrl(rs.getString("image_url"))
                    .relatedId(rs.getString("related_id"))
                    .startAt(rs.getDate("start_at"))
                    .endAt(rs.getDate("end_at"))
                    .build());
        }catch (Exception e){
            log.error("Error while getting notification list", e);
        }
        return Collections.emptyList();

    }

    public List<Notification> findByUserIdOrUserIdIsNull(String userId){
        try{
            String sql = String.format(
                    "SELECT id, user_id, message, title, image_url, related_id, start_at, end_at " +
                            "FROM movie_notification " +
                            "WHERE user_id =:userId AND status = 1 " +
                            "AND DATE(start_at) <= DATE(NOW()) " +
                            "AND DATE(end_at) >= DATE(NOW()) " +
                            "ORDER BY start_at DESC "
            );

            SqlParameterSource params = new MapSqlParameterSource("userId", userId);

            return jdbcTemplate.query(sql, params, (rs, rowNum) -> Notification.builder()
                    .id(rs.getLong("id"))
                    .userId(rs.getString("user_id"))
                    .message(rs.getString("message"))
                    .title(rs.getString("title"))
                    .imageUrl(rs.getString("image_url"))
                    .relatedId(rs.getString("related_id"))
                    .startAt(rs.getDate("start_at"))
                    .endAt(rs.getDate("end_at"))
                    .build());
        }catch (Exception e){
            log.error("Error while getting notification list", e);
        }
        return Collections.emptyList();
    }

    public Notification findById(Long id){
        try {
            String sql = String.format(
                    "SELECT id, user_id, message, title, image_url, related_id, start_at, end_at " +
                            "FROM movie_notification " +
                            "WHERE id =:id AND status = 1 " +
                            "AND DATE(start_at) <= DATE(NOW()) " +
                            "AND DATE(end_at) >= DATE(NOW()) " +
                            "ORDER BY start_at DESC "
            );

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("id", id);

            return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) ->
                    Notification.builder()
                            .id(rs.getLong("id"))
                            .userId(rs.getString("user_id"))
                            .message(rs.getString("message"))
                            .title(rs.getString("title"))
                            .imageUrl(rs.getString("image_url"))
                            .relatedId(rs.getString("related_id"))
                            .startAt(rs.getDate("start_at"))
                            .endAt(rs.getDate("end_at"))
                            .build());

        } catch (EmptyResultDataAccessException e) {
            log.warn("Notification not found with id: {}", id);
        } catch (Exception e) {
            log.error("Error retrieving Notification: {}", e.getMessage(), e);
        }
        return null;
    }
}
