package com.alibou.security.feature.favorite.dao;

import com.alibou.security.api.v1.dto.favorite.FavoriteWithMovieDto;
import com.alibou.security.feature.favorite.model.Favorite;
import com.alibou.security.feature.movie.model.Movie;
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
public class FavoriteDaoImpl implements FavoriteDao {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Favorite likeMovieByEmail(String email, String movieCode) {
        try {
            String checkSql = "SELECT COUNT(*) FROM favorite WHERE email = :email AND movie_code = :movieCode";
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("email", email)
                    .addValue("movieCode", movieCode);

            int count = jdbcTemplate.queryForObject(checkSql, params, Integer.class);

            if (count > 0) {
                String updateSql = "UPDATE favorite SET active = 1, favorite_day = NOW() WHERE email = :email AND movie_code = :movieCode";
                jdbcTemplate.update(updateSql, params);
            } else {
                String insertSql = "INSERT INTO favorite (email, movie_code, active, favorite_day) VALUES (:email, :movieCode, 1, NOW())";
                jdbcTemplate.update(insertSql, params);
            }

            String selectSql = "SELECT active, favorite_day, email, movie_code FROM favorite WHERE email = :email AND movie_code = :movieCode";
            return jdbcTemplate.queryForObject(selectSql, params, (rs, rowNum) -> Favorite.builder()
                    .active(rs.getBoolean("active"))
                    .favoriteDate(rs.getDate("favorite_day"))
                    .movieCode(rs.getString("movie_code"))
                    .email(rs.getString("email"))
                    .build());

        } catch (EmptyResultDataAccessException e) {
            log.warn("Favorite movie: {} with email: {} not found", movieCode, email);
        } catch (Exception e) {
            log.error("Error updating favorite: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Favorite unLikeMovieByEmail(String email, String movieCode) {
        try{
            String checkSql = "SELECT COUNT(*) FROM favorite WHERE email = :email AND movie_code = :movieCode AND active = 1";
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("email", email)
                    .addValue("movieCode", movieCode);

            int count = jdbcTemplate.queryForObject(checkSql, params, Integer.class);

            if (count > 0) {
                String updateSql = "UPDATE favorite SET active = 0, unfavorite_day = NOW() WHERE email = :email AND movie_code = :movieCode";
                jdbcTemplate.update(updateSql, params);
            } else {
                log.warn("Favorite movie: {} with email: {} not found", movieCode, email);
            }

            String selectSql = "SELECT active, unfavorite_day, email, movie_code FROM favorite WHERE email = :email AND movie_code = :movieCode";
            return jdbcTemplate.queryForObject(selectSql, params, (rs, rowNum) -> Favorite.builder()
                    .active(rs.getBoolean("active"))
                    .unfavoriteDate(rs.getDate("unfavorite_day"))
                    .movieCode(rs.getString("movie_code"))
                    .email(rs.getString("email"))
                    .build());

        } catch (EmptyResultDataAccessException e) {
            log.warn("Favorite movie: {} with email: {} not found", movieCode, email);
        } catch (Exception e) {
            log.error("Error updating favorite: {}", e.getMessage(), e);
        }
        return null;

    }

    @Override
    public Favorite getFavoriteByEmail(String email, String movieCode) {
        try{
            String sql = "SELECT active, email, movie_code FROM favorite WHERE email = :email AND movie_code = :movieCode";
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("email", email)
                    .addValue("movieCode", movieCode);

            return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) ->
                    Favorite.builder()
                            .active(rs.getBoolean("active"))
                            .email(rs.getString("email"))
                            .movieCode(rs.getString("movie_code"))
                            .build()
            );

        } catch (EmptyResultDataAccessException e) {
            log.warn("Favorite movie: {} with email: {} not found", movieCode, email);
        } catch (Exception e) {
            log.error("Error favorite: {}", e.getMessage(), e);
        }
        return null;
    }

//    @Override
//    public List<Favorite> getFavoriteActiveByEmailandMovielCode(String email, Integer page, Integer size) {
//        try{
//
//            int offset = Math.max((page - 1) * size, 0);
//
//            String sql = String.format("SELECT active, email, movie_code, favorite_day, unfavorite_day " +
//                    "FROM favorite " +
//                    "WHERE email = :email AND active = 1 " +
//                    "LIMIT %d OFFSET %d", size, offset );
//
//            SqlParameterSource params = new MapSqlParameterSource()
//                    .addValue("email", email);
//            return jdbcTemplate.query(sql, params, (rs, rowNum) -> Favorite.builder()
//                    .active(rs.getBoolean("active"))
//                    .email(rs.getString("email"))
//                    .movieCode(rs.getString("movie_code"))
//                    .favoriteDate(rs.getDate("favorite_day"))
//                    .unfavoriteDate(rs.getDate("unfavorite_day"))
//                    .build());
//
//        } catch (Exception e) {
//            log.error("Error retrieving favorite: {}", e.getMessage(), e);
//        }
//        return Collections.emptyList();
//    }
    @Override
    public List<FavoriteWithMovieDto> getFavoriteActiveByEmailandMovielCode(String email, Integer page, Integer size) {
        try {
            int offset = Math.max((page - 1) * size, 0);

            String sql = String.format("SELECT f.active, f.email, f.movie_code, f.favorite_day, f.unfavorite_day, " +
                    "m.movie_name, m.description, m.release_date, m.movie_genre, m.is_hot, m.image_url " +
                    "FROM favorite f " +
                    "JOIN movie m ON f.movie_code = m.movie_code " +
                    "WHERE f.email = :email AND f.active = 1 " +
                    "LIMIT %d OFFSET %d", size, offset);

            SqlParameterSource params = new MapSqlParameterSource().addValue("email", email);

            return jdbcTemplate.query(sql, params, (rs, rowNum) -> FavoriteWithMovieDto.builder()
                    .active(rs.getBoolean("active"))
                    .email(rs.getString("email"))
                    .movieCode(rs.getString("movie_code"))
                    .favoriteDate(rs.getDate("favorite_day"))
                    .unfavoriteDate(rs.getDate("unfavorite_day"))
                    .movieName(rs.getString("movie_name"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("release_date"))
                    .movieGenre(rs.getString("movie_genre"))
                    .isHot(rs.getInt("is_hot"))
                    .imageUrl(rs.getString("image_url"))
                    .build());

        } catch (Exception e) {
            log.error("Error retrieving favorite: {}", e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
